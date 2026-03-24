import socket


def decrypt(ct, key):
    ct = ct.lower()
    key = key.lower()
    key = key.replace(' ', '').replace('j', 'i')

    s = set()
    kc = []
    for ch in key:
        if ch not in s and ch.isalpha():
            s.add(ch)
            kc.append(ch)

    alp = 'abcdefghiklmnopqrstuvwxyz'
    for ch in alp:
        if ch not in s:
            kc.append(ch)
            s.add(ch)

    mat = []
    for i in range(5):
        mat.append(kc[i*5:(i+1)*5])

    dg = [ct[i:i+2] for i in range(0, len(ct), 2)]

    pt = []
    for d in dg:
        if len(d) < 2:
            break

        r1, c1 = 0, 0
        for i in range(5):
            for j in range(5):
                if mat[i][j] == d[0]: #inefficinefficient finding of character in the matrix
                    r1, c1 = i, j
                    break

        r2, c2 = 0, 0
        for i in range(5):
            for j in range(5):
                if mat[i][j] == d[1]:
                    r2, c2 = i, j
                    break

        if r1 == r2:
            pt.append(mat[r1][(c1 - 1) % 5])
            pt.append(mat[r2][(c2 - 1) % 5])
        elif c1 == c2:
            pt.append(mat[(r1 - 1) % 5][c1])
            pt.append(mat[(r2 - 1) % 5][c2])
        else:
            pt.append(mat[r1][c2])
            pt.append(mat[r2][c1])

    return ''.join(pt)


def start_server(host='127.0.0.1', port=65432):
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    server_socket.settimeout(1.0)  # adding time delay fro ctrl c to work


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

                        key = input("\nEnter key for decryption: ")

                        try:
                            plaintext = decrypt(ciphertext, key)
                            print(f"\nDecrypted plaintext: {plaintext}")

                        except Exception as e:
                            print(f"Decryption failed: {e}")
                            client_socket.send(b"Decryption failed")

                except Exception as e:
                    print(f"Error: {e}")
                finally:
                    client_socket.close()
                    print(f"\nConnection with {client_address} closed")

            except socket.timeout:
                continue

    except KeyboardInterrupt:
        print("\n\nServer shutting down...")
    finally:
        server_socket.close()


if __name__ == "__main__":
    start_server()
