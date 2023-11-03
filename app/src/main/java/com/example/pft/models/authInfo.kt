package com.example.pft.models

object authInfo {
    init {
        // Initialization code if needed
    }

    fun someFunction() {
        // Your singleton function code here
    }

    // Optional: Implement a getInstance method for consistency
    fun getInstance() = this
}
val singletonInstance = authInfo.getInstance()
//singletonInstance.someFunction()