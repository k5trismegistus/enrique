package com.example.keigo_yamamoto.repbyimg

import io.realm.RealmObject
import io.realm.annotations.Index

open class ResuImage: RealmObject() {

    open var filename: String = ""
    @Index open var keyword: String = ""

}