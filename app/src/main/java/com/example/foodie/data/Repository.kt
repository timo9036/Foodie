package com.example.foodie.data

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
//repo:primary constructor that takes a single parameter, remoteDataSource, which is an instance of the RemoteDataSource class
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource
) {
    // allows the Repository class to have access to the methods and properties of the RemoteDataSource class
    val remote = remoteDataSource
    val local = localDataSource

}