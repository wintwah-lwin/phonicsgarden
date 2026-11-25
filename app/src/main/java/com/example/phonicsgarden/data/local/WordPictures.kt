package com.example.phonicsgarden.data.local

/**
 * Emoji "pictures" for the words used in Feed the Monster.
 *
 * This doubles as a child-safety allowlist: a word is only ever shown to a child if it has an
 * entry here. Words fetched from the network that are not in this map are never displayed.
 */
object WordPictures {
    val emoji: Map<String, String> = mapOf(
        "sun" to "☀️", "sock" to "🧦", "snake" to "🐍",
        "ant" to "🐜", "apple" to "🍎",
        "tiger" to "🐯", "tent" to "⛺", "tree" to "🌳",
        "pig" to "🐷", "pen" to "🖊️", "pizza" to "🍕",
        "iguana" to "🦎",
        "net" to "🥅", "nut" to "🥜", "nose" to "👃",
        "moon" to "🌙", "mouse" to "🐭", "milk" to "🥛",
        "dog" to "🐶", "duck" to "🦆", "drum" to "🥁",
        "goat" to "🐐", "grapes" to "🍇", "gift" to "🎁",
        "octopus" to "🐙", "orange" to "🍊", "owl" to "🦉",
        "cat" to "🐱", "cow" to "🐄", "car" to "🚗",
        "kite" to "🪁", "key" to "🔑", "kangaroo" to "🦘",
        "egg" to "🥚", "elephant" to "🐘",
        "umbrella" to "☂️", "unicorn" to "🦄",
        "rabbit" to "🐰", "rainbow" to "🌈", "ring" to "💍",
        "hat" to "🎩", "horse" to "🐴", "house" to "🏠",
        "bus" to "🚌", "ball" to "⚽", "bee" to "🐝",
        "fish" to "🐟", "frog" to "🐸", "flower" to "🌸"
    )

    fun emojiFor(word: String): String? = emoji[word]
}
