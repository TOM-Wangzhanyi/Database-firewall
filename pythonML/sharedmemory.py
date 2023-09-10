import mmap
from threading import Semaphore

class SharedMemory(object):
    def __init__(self, name, size):
        self.shm = mmap.mmap(-1, size, name)
        self.sem = Semaphore(1)

    def acquire(self):
        self.sem.acquire()

    def release(self):
        self.sem.release()

    def put(self, offset, data):
        self.acquire()
        self.shm.seek(offset)
        self.shm.write(data)
        self.release()

    def get(self, offset, size):
        self.acquire()
        self.shm.seek(offset)
        data = self.shm.read(size)
        self.release()
        return data

if __name__ == "__main__":
    # 创建共享内存并获取信号量
    shm = SharedMemory("shm", 1024*1024) 
    shm.acquire()
    
    # 读Java程序写入的数据
    msg = shm.get(0, 100).decode() 
    print(f"Read: {msg}")
    
    # 释放信号量
    shm.release()
    
    # 暂停,确保Java程序已经等待信号量
  #  time.sleep(1)  
    
    # 再次获取信号量写入数据
    shm.acquire()
    shm.put(0, "Hello from Python!".encode())
    
    # 释放信号量
    shm.release()