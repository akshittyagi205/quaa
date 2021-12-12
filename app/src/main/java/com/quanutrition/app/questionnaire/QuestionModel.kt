package com.quanutrition.app.questionnaire

import java.util.*

class QuestionModel(var id: String, var name: String, var question: String, var text: String, var textName: String, var multiple: Int, var choices: ArrayList<ChoiceModel>) {

    class ChoiceModel(var id: String, var `val`: String, var selected: Int)

}