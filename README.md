# concurrent-learning

## 悲观锁与乐观锁

独占锁是一种悲观锁，悲观锁就是假定每次操作时都认为会产生冲突，synchronized 就是一种独占锁，会导致其它所有需要锁的线程挂起，等待持有锁的线程释放锁。
另一种锁是乐观锁，所谓乐观锁就是，每次操作时不加锁，而是假设没有冲突而去完成这次操作，如果因为冲突失败就会进行重试，直到成功为止。
乐观锁用到的机制就是 CAS(Compare And Swap)。