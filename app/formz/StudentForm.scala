package formz

import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText, number}

object StudentForm {
  val form: Form[StudentFormData] = Form(
    mapping(
      "name" -> nonEmptyText
        .verifying("too few chars", s => lengthIsGreaterThanNCharacters(s, 2))
        .verifying("too many chars", s => lengthIsLessThanNCharacters(s, 20)),
      "intervieCounter" -> number
        .verifying("Количество собеседований не может быть меньше 0", s => lengthIsGreaterThanNInt(s, 0))
        .verifying("Очень сомневаюсь что ты смог побывать на собеседованиях больше чем 10миллионов раз", s => lengthIsLessThanNInt(s, 10000000)),
      "javaRushLevel" -> number
        .verifying("Уровень JavaRush не может быть меньше 0", s => lengthIsGreaterThanNInt(s, 1))
        .verifying("Уровень JavaRush не может быть больше 41", s => lengthIsLessThanNInt(s, 10000000)),
      "chanceToComplete" -> number
        .verifying("Шанс комплита не может быть меньше 0", s => lengthIsGreaterThanNInt(s, 1))
        .verifying("Шанс комплита не может быть больше 100", s => lengthIsLessThanNInt(s, 100))
    )(StudentFormData.apply)(StudentFormData.unapply))

  private def lengthIsGreaterThanNCharacters(s: String, n: Int): Boolean = {
    if (s.length > n) true else false
  }

  private def lengthIsLessThanNCharacters(s: String, n: Int): Boolean = {
    if (s.length < n) true else false
  }

  private def lengthIsGreaterThanNInt(s: Int, n: Int): Boolean = {
    if (s >= n) true else false
  }

  private def lengthIsLessThanNInt(s: Int, n: Int): Boolean = {
    if (s < n) true else false
  }
}
