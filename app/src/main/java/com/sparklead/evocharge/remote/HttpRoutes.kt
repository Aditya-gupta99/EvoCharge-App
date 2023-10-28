package com.sparklead.evocharge.remote

object HttpRoutes {

    private const val BASEURL = "http://192.168.102.21:5000"

    const val SIGNUP = "$BASEURL/signUp"

    const val updateUser = "$BASEURL/user"

    const val SIGNIN = "$BASEURL/signIn"

    const val STATION_LIST = "$BASEURL/chargingStation/allList"
}