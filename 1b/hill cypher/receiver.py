import socket
import numpy as np
from sympy import Matrix

def decrypt(ciphertext, key_matrix):
    n = len(key_matrix)
    mod = 26
   
    key_matrix_sympy = Matrix(key_matrix)
    try:
        inv_key_matrix = key_matrix_sympy.inv_mod(mod) # pyright: ignore[reportAttributeAccessIssue] (added this so that vscode can shut up)
        inv_key_matrix = np.array(inv_key_matrix).astype(int)
    except:
        raise ValueError("Key matrix is not invertible modulo 26")
    
    cipher_vector = [ord(char) - ord('a') for char in ciphertext.lower()]

    plain_vector = []
    for i in range(0, len(cipher_vector), n):
        block = np.array(cipher_vector[i:i+n]).reshape(n, 1)
        decrypted_block = np.dot(inv_key_matrix, block) % mod
        plain_vector.extend(decrypted_block.flatten().tolist())
    
    plaintext = ''.join([chr(int(num) + ord('a')) for num in plain_vector])
    return plaintext


def start_server(host='127.0.0.1', port=65432):
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    server_socket.settimeout(1.0)  # Add timeout to allow KeyboardInterrupt to register even
    
    server_socket.bind((host, port))
    server_socket.listen(5)
    print(f"Server listening on {host}:{port}\n")
    
    try:
        while True:
            try:
                client_socket, client_address = server_socket.accept()
                print(f"Connection from {client_address}")
                
                try:
                    ciphertext = client_socket.recv(1024).decode('utf-8')
                    
                    if ciphertext:
                        print(f"\nReceived ciphertext: {ciphertext}")
                        
                        n = int(input("\nEnter size of key matrix (n x n): "))
                        
                        print(f"Enter {n*n} elements of key matrix (row by row):")
                        key_matrix = []
                        for i in range(n):
                            row = list(map(int, input(f"Row {i+1}: ").split()))
                            key_matrix.append(row)
                        
                        key_matrix = np.array(key_matrix)
                        
                        try:
                            plaintext = decrypt(ciphertext, key_matrix)
                            print(f"\nDecrypted plaintext: {plaintext}")
                            
                            response = f"Message decrypted successfully: {plaintext}"
                            client_socket.send(response.encode('utf-8'))
                        except ValueError as e:
                            print(f"Decryption failed: {e}")
                            client_socket.send(b"Decryption failed: Invalid key matrix")
                    
                except Exception as e:
                    print(f"Error: {e}")
                finally:
                    client_socket.close()
                    print(f"\nConnection with {client_address} closed")
                    print("-" * 50)
                    
            except socket.timeout:
                continue  
                
    except KeyboardInterrupt:
        print("\n\nServer shutting down...")
    finally:
        server_socket.close()


if __name__ == "__main__":
    start_server()
