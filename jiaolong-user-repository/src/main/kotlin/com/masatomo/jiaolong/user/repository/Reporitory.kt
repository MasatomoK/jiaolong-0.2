package com.masatomo.jiaolong.user.repository

import com.masatomo.jiaolong.core.common.IntegralId
import com.masatomo.jiaolong.core.repository.Repository
import com.masatomo.jiaolong.core.repository.exposed.ksp.GenerateAbstractExposedRepository
import com.masatomo.jiaolong.core.repository.exposed.ksp.GenerateExposedTable
import com.masatomo.jiaolong.core.repository.exposed.ksp.TableSeed
import com.masatomo.jiaolong.core.repository.ksp.GenerateAbstractOnMemoryRepository
import com.masatomo.jiaolong.user.domain.Name
import com.masatomo.jiaolong.user.domain.Password
import com.masatomo.jiaolong.user.domain.User
import com.masatomo.jiaolong.user.repository.UserTable.toUser
import com.masatomo.jiaolong.user.repository.UserTable.whereIdEqualsTo
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.update


@GenerateAbstractOnMemoryRepository
@GenerateAbstractExposedRepository
interface UserRepository : Repository<User, IntegralId<User>> {
    fun sampleMethod()
}


@GenerateExposedTable
internal class UserTableSeed : TableSeed<User, IntegralId<User>>


//internal class UserRepositoryImpl : AbstractOnMemoryUserRepository() {
//    override fun sampleMethod() {
//        TODO("Not yet implemented")
//    }
//}

internal class ExposedUserRepositoryImpl : UserRepository {
    override fun register(entity: User): IntegralId<User> = UserTable
        .insertAndGetId { body(it, entity) }
        .run { IntegralId(value) }

    override fun findAll(): Iterable<User> = UserTable
        .selectAll()
        .map { it.toUser() }

    override fun findById(id: IntegralId<User>): User? = UserTable
        .select(whereIdEqualsTo(id))
        .firstOrNull()
        ?.toUser()

    override fun update(entity: User) {
        UserTable.update(whereIdEqualsTo(entity)) { body(it, entity) }
    }

    override fun delete(id: IntegralId<User>) {
        UserTable.deleteWhere { UserTable.id eq id.value }
    }

    override fun sampleMethod() {
        TODO("Not yet implemented")
    }
}


internal object UserTable : LongIdTable() {
    private val name = varchar("name", 10)
    private val password = varchar("password", 10)

    fun ResultRow.toUser() = User(
        IntegralId(this[id].value),
        Name(this[name]),
        Password(this[password]),
    )

    fun whereIdEqualsTo(id: IntegralId<User>): SqlExpressionBuilder.() -> Op<Boolean> = {
        UserTable.id eq id.value
    }

    fun whereIdEqualsTo(entity: User): SqlExpressionBuilder.() -> Op<Boolean> = whereIdEqualsTo(entity.id)

    fun body(statement: UpdateBuilder<*>, entity: User) {
        statement[name] = entity.name.value
        statement[password] = entity.password.value
    }
}
