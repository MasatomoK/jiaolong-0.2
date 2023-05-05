package com.masatomo.jiaolong.core.system

import com.masatomo.jiaolong.core.domain.values.IntegralId
import com.masatomo.jiaolong.core.domain.values.StringId
import com.masatomo.jiaolong.core.server.JiaolongApplication


interface SystemAccessor {
    fun <T> getSystemState(key: StringId<SystemStatus>): T
    fun getProcessStates(
        applicationId: StringId<JiaolongApplication>? = null,
        processNo: IntegralId<JiaolongApplication>? = null,
    ): Map<StringId<JiaolongApplication>, Map<IntegralId<JiaolongApplication>, ProcessState>>

    fun getCurrentTimeMillis(): Long
}


interface SystemStatus

data class ProcessState(
    val status: ProcessStatus
)

enum class ProcessStatus {
    RUNNING,
    STOPEED
}
