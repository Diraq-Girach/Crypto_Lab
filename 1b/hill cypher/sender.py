import socket
import numpy as np

def encrypt(plaintext, key_matrix):
    n = len(key_matrix)
    mod = 26
    
    plaintext = plaintext.replace(" ", "").lower()
    
    plain_vector = [ord(char) - ord('a') for char in plaintext] #extracting the 0-26 values here (musch more elaborate here in python)
    
    while len(plain_vector) % n != 0:
        plain_vector.append(23)  # addig padding of x
    
    cipher_vector = []
    for i in range(0, len(plain_vector), n):    # doing the matmul here row by row 
        block = np.array(plain_vector[i:i+n]).reshape(n, 1) # need to keep it 2d for np.dot to work
        encrypted_block = np.dot(key_matrix, block) % mod
        cipher_vector.extend(encrypted_block.flatten().tolist()) # flattening back to a list
    
    ciphertext = ''.join([chr(int(num) + ord('a')) for num in cipher_vector])
    return ciphertext

def start_client(host='127.0.0.1', port=65432):

    plaintext = input("\nEnter plaintext: ")
        
        
        
    n = int(input("\nEnter size of key matrix: "))
        
    print(f"Enter {n*n} elements of key matrix (row by row seperated by spaces):")
    key_matrix = []
    for i in range(n):
        row = list(map(int, input(f"Row {i+1}: ").split()))
        key_matrix.append(row)
        
    key_matrix = np.array(key_matrix)
        
    ciphertext = encrypt(plaintext, key_matrix)
    print(f"Encrypted ciphertext: {ciphertext}")
        
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        
    try:
        client_socket.connect((host, port))
        print(f"\nConnected to server at {host}:{port}")
            
        client_socket.send(ciphertext.encode('utf-8'))
        print(f"Ciphertext sent to server")
            
        response = client_socket.recv(1024).decode('utf-8')
        print(f"\nServer response: {response}")
            
    except Exception as e:
        print(f"Error: {e}")
    finally:
        client_socket.close()
        print("-" * 50)


if __name__ == "__main__":
    start_client()
