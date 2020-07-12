package rocks.wintren.rency.util

import io.reactivex.Scheduler

interface RxSchedulers {

    fun main() : Scheduler

    fun io() : Scheduler

    fun computation() : Scheduler

}