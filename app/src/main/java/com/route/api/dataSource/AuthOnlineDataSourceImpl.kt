package com.route.api.dataSource

import com.google.gson.Gson
import com.route.api.ApiServices
import com.route.api.data.SignInRequestDto
import com.route.api.data.toSignUpRequestDto
import com.route.api.helpers.safeApiCallWithoutEnvelope
import com.route.data.dataSource.AuthOnlineDataSource
import com.route.domain.data.AuthSession
import com.route.domain.data.RegistrationDetails
import com.route.domain.data.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthOnlineDataSourceImpl @Inject constructor(
    private val apiServices: ApiServices,
    private val gson: Gson,
): AuthOnlineDataSource {

    override fun signIn(email: String, password: String): Flow<Resource<AuthSession?>> {
        return safeApiCallWithoutEnvelope(
            gson = gson,
            api = {
                apiServices.signIn(body = SignInRequestDto(email = email, password = password))
            },
            converter = { dto -> dto.toAuthSession() }
        )
    }

    override fun signUp(details: RegistrationDetails): Flow<Resource<AuthSession?>> {
        return safeApiCallWithoutEnvelope(
            gson = gson,
            api = {
                apiServices.signUp(body = details.toSignUpRequestDto())
            },
            converter = { dto -> dto.toAuthSession() }
        )
    }
}
