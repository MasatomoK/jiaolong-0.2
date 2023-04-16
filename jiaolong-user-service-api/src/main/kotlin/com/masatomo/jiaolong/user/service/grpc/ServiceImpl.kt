package com.masatomo.jiaolong.user.service.grpc

import com.masatomo.jiaolong.core.domain.values.IntegralId
import com.masatomo.jiaolong.user.domain.User
import com.masatomo.jiaolong.user.grpc.UserServiceOuterClass
import com.masatomo.jiaolong.user.grpc.findRequest
import com.masatomo.jiaolong.user.grpc.registerRequest
import com.masatomo.jiaolong.user.service.UserService

class UserServiceGrpcImpl(
    private val grpcClient: UserServiceClient
) : UserService {

    // register
    override suspend fun register(user: User): IntegralId<User> =
        registerRequestFrom(user)
            .calledWith(grpcClient)
            .toId()

    private fun registerRequestFrom(user: User) = registerRequest {
        this.user = user.toModel()
    }

    private suspend fun UserServiceOuterClass.RegisterRequest.calledWith(client: UserServiceClient) = client.register(this)
    private fun UserServiceOuterClass.RegisterResponse.toId() = id.toDomain<User>()


    // find
    override suspend fun find(id: IntegralId<User>): User {
        return findRequestFrom(id)
            .calledWith(grpcClient)
            .toUser()
    }

    private fun findRequestFrom(id: IntegralId<User>) = findRequest {
        this.id = id.value
    }

    private suspend fun UserServiceOuterClass.FindRequest.calledWith(client: UserServiceClient) = client.find(this)
    private fun UserServiceOuterClass.FindResponse.toUser() = user.toDomain()
}
