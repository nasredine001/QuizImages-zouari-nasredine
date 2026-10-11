# Quiz en images (Android • Kotlin • XML)

Quiz de 5 questions illustrées (thème : formes géométriques). Trois boutons de réponse par question,
score en haut à droite, barre de progression, écran de résultat final et bouton **Rejouer**.

## Ouvrir et lancer
1. Ouvrir Android Studio (Koala ou plus récent) → **File > Open** → choisir ce dossier.
2. Laisser la synchronisation Gradle se terminer (JDK 17, fourni avec Android Studio).
3. Lancer sur un émulateur ou un appareil (API 24+) avec **Run ▶**.

> Gradle Wrapper : si `gradlew` et `gradle/wrapper/gradle-wrapper.jar` sont absents, exécuter une fois
> `gradle wrapper --gradle-version 8.7` dans ce dossier, puis les ajouter au dépôt.

## Contenu du dépôt
- `app/` : code Kotlin (`MainActivity.kt`, `Question.kt`), layout (`activity_main.xml`) et images (`res/drawable`)
- `captures/` : captures d'écran (question, bonne réponse, mauvaise réponse, écran final)
- `compte-rendu/` : compte rendu du travail (`compte-rendu.md`)
