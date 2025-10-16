package cl.duoc.levelappchile.core.util

fun isValidEmail(email: String) = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
fun isValidPassword(pw: String) = pw.length >= 6