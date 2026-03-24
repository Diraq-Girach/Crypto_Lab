import socket


def encrypt(pt, key):
    pt = pt.lower()
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

    print("\nPlayfair Matrix:\n")
    for row in mat:
        print(" ".join(row))
    print()

    txt = pt.replace(' ', '').replace('j', 'i')
    txt = ''.join([c for c in txt if c.isalpha()])

    dg = []
    i = 0
    while i < len(txt):
        a = txt[i]
        b = txt[i+1] if i+1 < len(txt) else 'x'
        if a == b:
            dg.append(a + 'x')
            i += 1
        else:
            dg.append(a + b)
            i += 2

    ct = []
    for d in dg:
        r1, c1 = 0, 0
        for i in range(5):
            for j in range(5):
                if mat[i][j] == d[0]:
                    r1, c1 = i, j
                    break

        r2, c2 = 0, 0
        for i in range(5):
            for j in range(5):
                if mat[i][j] == d[1]:
                    r2, c2 = i, j
                    break

        if r1 == r2:
            ct.append(mat[r1][(c1 + 1) % 5])
            ct.append(mat[r2][(c2 + 1) % 5])
        elif c1 == c2:
            ct.append(mat[(r1 + 1) % 5][c1])
            ct.append(mat[(r2 + 1) % 5][c2])
        else:
            ct.append(mat[r1][c2])
            ct.append(mat[r2][c1])

    return ''.join(ct)


def start_client(host='127.0.0.1', port=65432):
    plaintext = input("\nEnter plaintext: ")
    key = input("Enter key: ")

    ciphertext = encrypt(plaintext, key)
    print(f"\nEncrypted ciphertext: {ciphertext}")

    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    try:
        client_socket.connect((host, port))
        print(f"\nConnected to server at {host}:{port}")

        client_socket.send(ciphertext.encode('utf-8'))
        print(f"Ciphertext sent to server")

    except Exception as e:
        print(f"Error: {e}")
    finally:
        client_socket.close()


if __name__ == "__main__":
    start_client()
