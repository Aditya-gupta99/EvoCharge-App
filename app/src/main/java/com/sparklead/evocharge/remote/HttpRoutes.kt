package com.sparklead.evocharge.remote

object HttpRoutes {

    private const val BASEURL = "http://192.168.165.21:5000"

    const val SIGNUP = "$BASEURL/signUp"

    const val updateUser = "$BASEURL/user"

    const val SIGNIN = "$BASEURL/signIn"
}