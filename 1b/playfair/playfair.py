def encrypt(pt, key):
    pt.lower()
    key.lower()
    key = key.lower().replace(' ', '').replace('j', 'i') # replacing j with i
    s = set()
    kc = []
    for ch in key:
        if ch not in s and ch.isalpha():
            s.add(ch)
            kc.append(ch)
    alp = 'abcdefghiklmnopqrstuvwxyz' # keeping  i in place of i/j
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
    print("\n")
    
    txt = pt.lower().replace(' ', '').replace('j', 'i')
    txt = ''.join([c for c in txt if c.isalpha()])
    
    dg = []
    i = 0
    while i < len(txt):
        a = txt[i]
        b = txt[i+1] if i+1 < len(txt) else 'x' # adding padding 
        if a == b:
            dg.append(a + 'x') #handling same letters
            i += 1
        else:
            dg.append(a + b)
            i += 2

    ct = []
    for d in dg:
        r1, c1 = 0, 0
        for i in range(5):
            for j in range(5):
                if mat[i][j] == d[0]:  #inefficinefficient finding of character in the matrix
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


def decrypt(ct, key):
    ct.lower()
    key.lower()
    key = key.lower().replace(' ', '').replace('j', 'i')
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
    
    ct = ct.lower()
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


if __name__ == "__main__":    
    pt = input("Enter plaintext: ")
    key = input("Enter key: ")
    
    enc = encrypt(pt, key)
    print(f"Ciphertext: {enc}")
    
    dec = decrypt(enc, key)
    print(f"Decrypted:  {dec}")
