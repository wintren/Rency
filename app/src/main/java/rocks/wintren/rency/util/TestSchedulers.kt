package rocks.wintren.rency.util

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class TestSchedulers : rocks.wintren.rency.util.RxSchedulers {

    override fun main(): Scheduler = Schedulers.trampoline()

    override fun io(): Scheduler = Schedulers.trampoline()

    override fun computation(): Scheduler = Schedulers.trampoline()

}