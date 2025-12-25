package com.github.seepick.kaml.k8s

import com.github.seepick.kaml.Kaml
import com.github.seepick.kaml.Konfig
import com.github.seepick.kaml.XKaml

val Kaml.k8s get() = K8s
val XKaml.k8s get() = XK8s(konfig)

object K8s // via extensions
class XK8s(val konfig: Konfig = Konfig.default)
