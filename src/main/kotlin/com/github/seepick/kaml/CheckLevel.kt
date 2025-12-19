package com.github.seepick.kaml

enum class CheckLevel {
    doNothing, logWarning, raiseError;
}

interface Checkable {
    // checkLevel, collect issues; act accordingly
}
