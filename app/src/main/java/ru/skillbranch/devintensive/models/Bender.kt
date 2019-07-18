package ru.skillbranch.devintensive.models


class Bender(
    var status: Status = Status.NORMAL,
    var question: Question = Question.NAME
) {

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {

        if (question == Question.IDLE) {
            return question.question to status.color
        }

        if (answer != "") {
            val validMsg = question.validate(answer)
            if (validMsg != null) {
                return "${validMsg}\n${question.question}" to status.color
            }
        }

        return if (question.answers.contains(answer.toLowerCase())) {
            question = question.nextQuestion()
            "Отлично - ты справился\n${question.question}" to status.color
        } else {
            var answ: String = "Это неправильный ответ"

            if (status == Status.CRITICAL) {
                answ = "Это неправильный ответ. Давай все по новой"
            }
            status = status.nextStatus()

            "${answ}\n${question.question}" to status.color
        }

    }

    enum class Status(
        val color: Triple<Int, Int, Int>
    ) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex) {
                values()[this.ordinal+1]
            } else {
                values()[0]
            }
        }
    }


    enum class Question(
        val question: String,
        val answers: List<String>
    ) {
        NAME("Как меня зовут?", listOf("бендер", "bender")) {
            override fun nextQuestion(): Question = PROFESSION

            override fun validate(answer: String): String? {

                if (answer.first().isLowerCase()) {
                    return "Имя должно начинаться с заглавной буквы"
                }
                return null
            }
        },

        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")){
            override fun nextQuestion(): Question = MATERIAL

            override fun validate(answer: String): String? {
                if (answer.first().isUpperCase()) {
                    return "Профессия должна начинаться со строчной буквы"
                }
                return null
            }
        },

        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")){
            override fun nextQuestion(): Question = BDAY

            override fun validate(answer: String): String? {
                if (answer.contains("\\d+".toRegex())) {
                    return "Материал не должен содержать цифр"
                }
                return null
            }
        },

        BDAY("Когда меня создали?", listOf("2993")){
            override fun nextQuestion(): Question = SERIAL

            override fun validate(answer: String): String? {
                if (answer.contains("[^0-9]+".toRegex())) {
                    return "Год моего рождения должен содержать только цифры"
                }
                return null
            }
        },

        SERIAL("Мой серийный номер?", listOf("2716057")){
            override fun nextQuestion(): Question = IDLE

            override fun validate(answer: String): String? {
                if (answer.contains("[^0-9]+".toRegex()) || answer.length != 7) {
                    return "Серийный номер содержит только цифры, и их 7"
                }
                return null
            }
        },

        IDLE("На этом все, вопросов больше нет", listOf()){
            override fun nextQuestion(): Question = IDLE

            override fun validate(answer: String): String? {
                return null
            }
        };

        abstract fun nextQuestion(): Question
        abstract fun validate(answer: String): String?
    }
}