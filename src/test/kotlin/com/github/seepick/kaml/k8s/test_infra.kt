package com.github.seepick.kaml.k8s

import com.github.seepick.kaml.Image

fun Image.Companion.any() = Image.Companion("anyName", "anyGroup", "anyVersion")
