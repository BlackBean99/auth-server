# README

# π₯οΈνλ‘μ νΈ μκ°

---

SmileGate SpringBoot MSA μΈμ¦μλ² κ³Όμ μλλ€.

λ‘κ·ΈμΈ, λ‘κ·Έμμ, νμκ°μ, μ μ  κ΄λ¦¬, κ΄λ¦¬μ κΆν κ΄λ¦¬ κΈ°λ₯μ΄ μμ΅λλ€.

## β±οΈκ°λ°κΈ°κ°

---

2022.12.19 ~ 2022.12.25

## π¨How to Run

---

```bash
cd deploy
./script.sh
```

## κ°λ° νκ²½

- docker-compose
- docker
- SpringBoot 3.0.0
- Java 17
- netflix-eureka-server
- netflix-eureka-client
- spring-security
- jwt
- jsp

# μν€νμ²

μ μ²΄ μν€νμ²
![3](https://user-images.githubusercontent.com/54030889/209473210-f91616a5-e790-4f73-bf37-a0ec0c48b06d.png)

### λ΄λΆ μν€νμ²

μΈμ¦ μλ² λ΄λΆλ ν₯μ¬κ³ λ  μν€νμ²λ‘ μμ‘΄μ± λΆλ¦¬λ₯Ό μ΄μ μΌλ‘ κ΅¬ν


![1](https://user-images.githubusercontent.com/54030889/209473180-ff1f6d48-8d10-40bf-bc97-d692e41dd29e.png)


- Eureka μλ²μ λ±λ‘μμΌμ μ€μ  ν΅μ μ GateWay ν΅ν΄μ λΌμ°νλλ€,

# κΈ°λ₯ λͺμΈ

# Domain κ΅¬μΆ

## User parameters

- BaseTimeEntity
- id
- name
- password
- email
- role
- isAccountNonExpired : κ³μ μ΄ λ§λ£λμ§ μμλμ§
- isAccountNonLocked : κ³μ μ΄ μ κ²¨μμ§ μμμ§
- isCredintialNonExpired : κ³μ μ ν¨μ€μλκ° λ§λ£λμ§ μμλμ§
- isEnabled : κ³μ μ΄ μ¬μ©κ°λ₯ν κ³μ μΈμ§
- `Collection<? extends GrantedAuthority> authorities`
- DisabledException : κ³μ λΉνμ±ν

# κΈ°λ₯ κ΅¬ν

### Account

- νμκ°μ
- νμ κ°μμ μνΈν
- μ΄λ©μΌμ€λ³΅νμΈ
- λ‘κ·ΈμΈ
- Token μ ν¨ νμΈ ( checkValidToken )
- λ‘κ·Έμμ
- reIssue
- λΉλ°λ²νΈ Input λλ¬Έμ, μλ¬Έμ, νΉμλ¬Έμ νμΈ
- νμκ°μ ν μ΄λ©μΌ μΈμ¦ μ isCredentialNonExpired, isEnabled  true λ‘ λ³κ²½
- μλ λ‘κ·ΈμΈ ( λ‘κ·ΈμΈ μν μ μ§ )
- λ‘κ·ΈμΈ μ€ν¨κ²½μ° λ€λ₯Έ λ°ν
    - μμ΄λ λΉλ°λ²νΈκ° μΌμΉνμ§ μμ κ²½μ°
    - μ κ·Ό κΆνμ΄ μλ κ³μ μΌ κ²½μ°
    - νμ¬ μ¬μ©ν  μ μλ κ²½μ°μΌ κ²½μ°
    - FrailureHandler μ°λ

### User

- μ μ  μ μ²΄ μ‘°ν
- μ μ  νλͺ λ¨μ μ‘°ν
- μ΄λ¦μΌλ‘ μ μ  κ²μ
- νμ μ­μ  - νμ expired β trueλ‘ λ³κ²½
- μ μ  νμμ λ³΄ μμ 