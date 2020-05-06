package com.sphtech.db

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table

@Table(name="sphtech_mobiledata")
class DBData: Model() {

    @Column(name="data")
    var data:String = ""
}