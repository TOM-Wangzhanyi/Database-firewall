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
    # ���������ڴ沢��ȡ�ź���
    shm = SharedMemory("shm", 1024*1024) 
    shm.acquire()
    
    # ��Java����д�������
    msg = shm.get(0, 100).decode() 
    print(f"Read: {msg}")
    
    # �ͷ��ź���
    shm.release()
    
    # ��ͣ,ȷ��Java�����Ѿ��ȴ��ź���
  #  time.sleep(1)  
    
    # �ٴλ�ȡ�ź���д������
    shm.acquire()
    shm.put(0, "Hello from Python!".encode())
    
    # �ͷ��ź���
    shm.release()