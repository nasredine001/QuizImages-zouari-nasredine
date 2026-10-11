# Compte rendu — Quiz en images

**Étudiant(s) :** _à compléter_  
**Thème choisi :** formes géométriques (triangle, carré, cercle, étoile, rectangle)

## 1. Interface et widgets
`activity_main.xml` : bandeau fixe (`txt_titre`, `txt_score`), `ProgressBar` horizontale (`progress_quiz`),
`ScrollView` contenant `txt_numero`, `img_question`, `txt_question`, `btn_choix1..3`, `txt_feedback`,
`btn_suivant`, ainsi qu'une zone de résultat (`layout_resultat`, `txt_resultat`, `btn_rejouer`).

## 2. Tableau des questions
Une `data class Question(texte, imageRes, choix, indiceBonneReponse, descriptionImage)` et un
`Array<Question>` de 5 éléments dans `MainActivity`. Chaque image est un drawable vectoriel
(`R.drawable.triangle`, etc.). La bonne réponse est repérée par son indice (0, 1 ou 2), variable selon les questions.

## 3. Événements, score et progression
Chaque bouton appelle `onChoiceClicked(index)` : l'indice du bouton est le choix sélectionné. Le booléen `answered`
bloque toute seconde validation (un seul point max) et les trois boutons sont désactivés. Le **score** compte les
bonnes réponses ; la **progression** compte les réponses données (`currentIndex` + 1 si répondu, 5 à la fin).
À la dernière question, le bouton devient « Voir le résultat » et `finished` passe à vrai.

## 4. Tests réalisés
_Remplir après avoir exécuté chaque scénario sur l'émulateur._

| Test | Résultat attendu | Résultat obtenu |
|---|---|---|
| Nouvelle partie | 0/5, progression 0/5 | |
| Bonne réponse | score +1, progression +1 | |
| Mauvaise réponse | score inchangé, progression +1 | |
| Clics multiples | aucun point en plus, boutons désactivés | |
| 5 bonnes / 5 fausses / mélange | 5/5, 0/5, score exact | |
| Rejouer | retour à la question 1, tout remis à zéro | |
| Petit écran / rotation | choix accessibles, état conservé | |

## 5. Difficultés rencontrées
_à compléter_
