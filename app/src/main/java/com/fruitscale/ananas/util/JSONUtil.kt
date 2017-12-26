package com.fruitscale.ananas.util

import org.json.JSONArray

fun JSONArray.toList(): List<Any> = (0..length()).map { this[it] }
