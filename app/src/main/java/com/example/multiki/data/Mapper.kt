package com.example.multiki.data

import com.example.multiki.domain.Animation

fun AnimationDbModel.mapToEntity(): Animation = Animation(
    createAt = this.createAt,
    fileName = this.fileName
)

fun Animation.mapToDb(): AnimationDbModel = AnimationDbModel(
    createAt = this.createAt,
    fileName = this.fileName
)