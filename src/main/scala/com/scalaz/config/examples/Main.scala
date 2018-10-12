package com.scalaz.config

package examples

import com.scalaz.config.ConfigError.ErrorType
import scalaz.{ -\/, NonEmptyList, \/, \/- }
import scalaz.syntax.equal._
import scalaz.std.string._
import scalaz.effect.IO
import scalaz.syntax.std.boolean._

object Main extends App {
  final case class EnvVar1(s: String) extends AnyVal
  final case class EnvVar2(s: String) extends AnyVal

  case class SampleConfig(s1: EnvVar1, s2: EnvVar2)

  implicit val propertyEnvVar1: Property[EnvVar1] = new Property[EnvVar1] {
    override def show(a: EnvVar1): PropertyValue = a.s

    override def read(p: PropertyValue): ErrorType \/ EnvVar1 =
      (p === "right").either(EnvVar1(p)).or(ConfigError.InvalidValue(p, "right"))

    override def document: String =
      "This is first property from the environment that can only be right."
  }

  implicit val propertyEnvVar2: Property[EnvVar2] = new Property[EnvVar2] {
    override def show(a: EnvVar2): PropertyValue = a.s

    override def read(p: PropertyValue): ErrorType \/ EnvVar2 =
      (p === "right2").either(EnvVar2(p)).or(ConfigError.InvalidValue(p, "right2"))

    override def document: String =
      "This is second property from the environment that can only be right."
  }

  import Config._

  def config[F[_]] = new Config[F, SampleConfig] {

    val equiv: Equiv[(EnvVar1, EnvVar2), SampleConfig] = Equiv[EnvVar1 ~ EnvVar2, SampleConfig](
      a => SampleConfig(a._1, a._2),
      s => s.s1 -> s.s2
    )

    def apply(implicit F: ConfigSyntax[F]): F[SampleConfig] =
      (read[F, EnvVar1]("envvar") ~ read[F, EnvVar2]("envvar2")).map(equiv)
  }

  val mapReader: MapReader[SampleConfig] = Config.reader(config)

  //  User will be already be in the context of IO (ZIO potentially)
  val configParsing = IO.apply(sys.env).map(mapReader(_))

  // If config doesn't exist in env
  // Immediate issue is max error accumulation didn't take place
  val parsed = configParsing.unsafePerformIO()
  assert(parsed == -\/(NonEmptyList(ConfigError("envvar", ConfigError.MissingValue))))

  // If config exists in the env, and they are valid
  val validConfig = Map("envvar" -> "right", "envvar2" -> "right2")
  assert(mapReader(validConfig) == \/-(SampleConfig(EnvVar1("right"), EnvVar2("right2"))))

  val invalidConfig = Map("envvar" -> "wrong")
  assert(
    mapReader(invalidConfig) == -\/(
      NonEmptyList(ConfigError("envvar", ConfigError.InvalidValue("wrong", "right")))
    )
  )
}
