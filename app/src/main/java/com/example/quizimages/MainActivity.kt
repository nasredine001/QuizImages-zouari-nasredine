package com.example.quizimages

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.quizimages.databinding.ActivityMainBinding

/**
 * Unique activité du quiz en images (cinq questions statiques, trois choix chacune).
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Les cinq questions statiques du quiz (thème : formes géométriques).
    // La bonne réponse n'est volontairement pas toujours au même emplacement.
    private val questions: Array<Question> = arrayOf(
        Question(
            texte = "Combien de côtés possède la forme affichée ?",
            imageRes = R.drawable.triangle,
            choix = arrayOf("4", "3", "5"),
            indiceBonneReponse = 1,
            descriptionImage = "Un triangle rouge"
        ),
        Question(
            texte = "Quelle est cette forme à quatre côtés égaux ?",
            imageRes = R.drawable.carre,
            choix = arrayOf("Cercle", "Triangle", "Carré"),
            indiceBonneReponse = 2,
            descriptionImage = "Un carré bleu"
        ),
        Question(
            texte = "Quelle forme n'a ni côté droit ni sommet ?",
            imageRes = R.drawable.cercle,
            choix = arrayOf("Cercle", "Carré", "Étoile"),
            indiceBonneReponse = 0,
            descriptionImage = "Un cercle vert"
        ),
        Question(
            texte = "Combien de branches possède cette étoile ?",
            imageRes = R.drawable.etoile,
            choix = arrayOf("6", "4", "5"),
            indiceBonneReponse = 2,
            descriptionImage = "Une étoile jaune à cinq branches"
        ),
        Question(
            texte = "Quelle forme est représentée sur l'image ?",
            imageRes = R.drawable.rectangle,
            choix = arrayOf("Triangle", "Rectangle", "Cercle"),
            indiceBonneReponse = 1,
            descriptionImage = "Un rectangle violet"
        )
    )

    private val total: Int get() = questions.size

    // --- État du jeu ---
    private var currentIndex = 0      // indice de la question courante
    private var score = 0             // nombre de bonnes réponses
    private var answered = false      // la question courante a-t-elle reçu une réponse ?
    private var selectedChoice = -1   // choix sélectionné (-1 = aucun)
    private var finished = false      // la partie est-elle terminée ?

    private lateinit var choiceButtons: List<Button>
    private var defaultButtonTint: ColorStateList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        choiceButtons = listOf(binding.btnChoix1, binding.btnChoix2, binding.btnChoix3)
        defaultButtonTint = binding.btnChoix1.backgroundTintList

        // L'indice du bouton cliqué indique le choix sélectionné.
        choiceButtons.forEachIndexed { index, button ->
            button.setOnClickListener { onChoiceClicked(index) }
        }
        binding.btnSuivant.setOnClickListener { onNextClicked() }
        binding.btnRejouer.setOnClickListener { restartQuiz() }

        // Restauration après rotation de l'écran
        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_INDEX, 0).coerceIn(0, total - 1)
            score = savedInstanceState.getInt(KEY_SCORE, 0).coerceIn(0, total)
            answered = savedInstanceState.getBoolean(KEY_ANSWERED, false)
            selectedChoice = savedInstanceState.getInt(KEY_SELECTED, -1)
            finished = savedInstanceState.getBoolean(KEY_FINISHED, false)
        }

        if (finished) showResult() else showQuestion()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_INDEX, currentIndex)
        outState.putInt(KEY_SCORE, score)
        outState.putBoolean(KEY_ANSWERED, answered)
        outState.putInt(KEY_SELECTED, selectedChoice)
        outState.putBoolean(KEY_FINISHED, finished)
    }

    /** Affiche la question courante (et la correction si elle a déjà été validée). */
    private fun showQuestion() {
        val question = questions[currentIndex]

        binding.layoutQuiz.visibility = View.VISIBLE
        binding.layoutResultat.visibility = View.GONE

        binding.txtNumero.text = getString(R.string.numero_format, currentIndex + 1, total)
        binding.imgQuestion.setImageResource(question.imageRes)
        binding.imgQuestion.contentDescription = question.descriptionImage
        binding.txtQuestion.text = question.texte

        choiceButtons.forEachIndexed { index, button ->
            button.text = question.choix[index]
            button.isEnabled = true
            button.backgroundTintList = defaultButtonTint
        }
        binding.txtFeedback.visibility = View.GONE
        binding.btnSuivant.visibility = View.GONE

        if (answered) showAnswerState()
        updateHeader()
    }

    /** Valide la réponse correspondant au bouton cliqué. */
    private fun onChoiceClicked(choiceIndex: Int) {
        // Garde-fou : une seule validation par question, donc un seul point maximum.
        if (answered || finished) return

        answered = true
        selectedChoice = choiceIndex
        if (choiceIndex == questions[currentIndex].indiceBonneReponse) {
            score++
        }
        showAnswerState()
        updateHeader()
    }

    /** Désactive les choix, affiche la correction et le bouton de navigation. */
    private fun showAnswerState() {
        val question = questions[currentIndex]
        val correct = question.indiceBonneReponse
        val green = ContextCompat.getColor(this, R.color.vert_juste)
        val red = ContextCompat.getColor(this, R.color.rouge_faux)

        choiceButtons.forEach { it.isEnabled = false }
        choiceButtons[correct].backgroundTintList = ColorStateList.valueOf(green)

        if (selectedChoice == correct) {
            binding.txtFeedback.text = getString(R.string.bonne_reponse)
            binding.txtFeedback.setTextColor(green)
        } else {
            if (selectedChoice in choiceButtons.indices) {
                choiceButtons[selectedChoice].backgroundTintList = ColorStateList.valueOf(red)
            }
            binding.txtFeedback.text =
                getString(R.string.mauvaise_reponse_format, question.choix[correct])
            binding.txtFeedback.setTextColor(red)
        }
        binding.txtFeedback.visibility = View.VISIBLE

        binding.btnSuivant.text = getString(
            if (currentIndex == total - 1) R.string.voir_resultat else R.string.question_suivante
        )
        binding.btnSuivant.visibility = View.VISIBLE
    }

    /** Passe à la question suivante ou affiche le résultat final. */
    private fun onNextClicked() {
        if (!answered) return // impossible d'avancer sans avoir répondu

        if (currentIndex >= total - 1) {
            finished = true
            showResult()
        } else {
            currentIndex++
            answered = false
            selectedChoice = -1
            showQuestion()
        }
    }

    /** Affiche le score final dans la même activité et masque les choix. */
    private fun showResult() {
        binding.layoutQuiz.visibility = View.GONE
        binding.layoutResultat.visibility = View.VISIBLE
        binding.txtResultat.text = getString(R.string.resultat_format, score, total)
        binding.txtMessageFinal.text = when {
            score == total -> "Parfait ! Toutes les réponses sont justes."
            score >= 3 -> "Bien joué !"
            score >= 1 -> "Pas mal, réessayez pour progresser."
            else -> "Aucune bonne réponse, retentez votre chance !"
        }
        updateHeader()
    }

    /** Réinitialise complètement le quiz. */
    private fun restartQuiz() {
        currentIndex = 0
        score = 0
        answered = false
        selectedChoice = -1
        finished = false
        showQuestion()
    }

    /** Met à jour le score et la barre de progression (nombre de réponses DONNÉES). */
    private fun updateHeader() {
        binding.txtScore.text = getString(R.string.score_format, score, total)
        val answeredCount = when {
            finished -> total
            answered -> currentIndex + 1
            else -> currentIndex
        }
        binding.progressQuiz.max = total
        binding.progressQuiz.progress = answeredCount
    }

    private companion object {
        const val KEY_INDEX = "key_index"
        const val KEY_SCORE = "key_score"
        const val KEY_ANSWERED = "key_answered"
        const val KEY_SELECTED = "key_selected"
        const val KEY_FINISHED = "key_finished"
    }
}
