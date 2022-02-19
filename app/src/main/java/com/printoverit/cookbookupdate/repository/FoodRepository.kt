package com.printoverit.cookbookupdate.repository

import com.printoverit.cookbookupdate.data.LocalDataSource
import com.printoverit.cookbookupdate.data.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import java.security.PrivateKey
import javax.inject.Inject

@ActivityRetainedScoped
class FoodRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {

    val remote= remoteDataSource
    val local=localDataSource

}