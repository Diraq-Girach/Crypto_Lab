import numpy as np
from sympy import Matrix


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


def main():
    plaintext = input("Enter plaintext: ")
    
    n = int(input("Enter size of key matrix (n x n): "))
    
    print(f"Enter {n*n} elements of key matrix (row by row):")
    key_matrix = []
    for i in range(n):
        row = list(map(int, input(f"Row {i+1}: ").split()))
        key_matrix.append(row)
    
    key_matrix = np.array(key_matrix)
    
    ciphertext = encrypt(plaintext, key_matrix)
    print(f"\nCiphertext: {ciphertext}")

    decrypted_text = decrypt(ciphertext, key_matrix)
    print(f"Decrypted text: {decrypted_text}")


if __name__ == "__main__":
    main()
