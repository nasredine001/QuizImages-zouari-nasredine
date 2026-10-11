package com.example.quizimages

import androidx.annotation.DrawableRes

/**
 * Représente une question du quiz.
 *
 * @property texte              énoncé de la question
 * @property imageRes           identifiant de la ressource drawable associée (ex. R.drawable.triangle)
 * @property choix              exactement trois réponses possibles
 * @property indiceBonneReponse indice (0, 1 ou 2) du choix correct dans [choix]
 * @property descriptionImage   texte d'accessibilité (contentDescription) de l'image
 */
data class Question(
    val texte: String,
    @DrawableRes val imageRes: Int,
    val choix: Array<String>,
    val indiceBonneReponse: Int,
    val descriptionImage: String
)
