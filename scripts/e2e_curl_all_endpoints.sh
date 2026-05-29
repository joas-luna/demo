#!/usr/bin/env bash
set -euo pipefail

BASE_URL="${BASE_URL:-http://localhost:8081}"

pass_count=0
fail_count=0

tmp_dir="$(mktemp -d)"
trap 'rm -rf "$tmp_dir"' EXIT

json_field() {
  local file="$1"
  local key="$2"
  python3 - <<'PY' "$file" "$key"
import json,sys
path,key=sys.argv[1],sys.argv[2]
with open(path, 'r', encoding='utf-8') as f:
    data=json.load(f)
value=data.get(key)
print("" if value is None else value)
PY
}

assert_http() {
  local name="$1"
  local method="$2"
  local endpoint="$3"
  local expected="$4"
  local body="${5:-}"

  local out_file="$tmp_dir/$(echo "$name" | tr ' ' '_' | tr -cd '[:alnum:]_').json"
  local code

  if [[ -n "$body" ]]; then
    code="$(curl -sS -o "$out_file" -w "%{http_code}" -X "$method" "$BASE_URL$endpoint" -H "Content-Type: application/json" -d "$body")"
  else
    code="$(curl -sS -o "$out_file" -w "%{http_code}" -X "$method" "$BASE_URL$endpoint")"
  fi

  if [[ "$code" == "$expected" ]]; then
    echo "[OK]  $name -> $code"
    pass_count=$((pass_count + 1))
  else
    echo "[ERR] $name -> esperado $expected, veio $code"
    echo "      endpoint: $method $endpoint"
    echo "      body: $(cat "$out_file")"
    fail_count=$((fail_count + 1))
  fi

  LAST_BODY_FILE="$out_file"
}

echo "Base URL: $BASE_URL"
echo "--- Iniciando testes de sucesso/falha para todos os controllers ---"

# USUARIO
assert_http "usuario_create_success" "POST" "/usuarios" "201" '{"nome":"U1","email":"u1@example.com","senha":"123456","pais":"Brasil","endereco":null,"pets":[]}'
usuario1_id="$(json_field "$LAST_BODY_FILE" "id")"

assert_http "usuario_list_success" "GET" "/usuarios" "200"
assert_http "usuario_get_success" "GET" "/usuarios/$usuario1_id" "200"
assert_http "usuario_put_success" "PUT" "/usuarios/$usuario1_id" "200" '{"nome":"U1 atualizado","email":"u1a@example.com","senha":"abcdef","pais":"Brasil","endereco":null,"pets":[]}'
assert_http "usuario_delete_success" "DELETE" "/usuarios/$usuario1_id" "204"

assert_http "usuario_get_fail_404" "GET" "/usuarios/999999" "404"
assert_http "usuario_put_fail_404" "PUT" "/usuarios/999999" "404" '{"nome":"Nao Existe","email":"nao@existe.com","senha":"x","pais":"BR","endereco":null,"pets":[]}'
assert_http "usuario_delete_fail_404" "DELETE" "/usuarios/999999" "404"

# Prepara usuario para ENDERECO, PET e CONSULTA
assert_http "usuario2_create_success" "POST" "/usuarios" "201" '{"nome":"U2","email":"u2@example.com","senha":"123456","pais":"Brasil","endereco":null,"pets":[]}'
usuario2_id="$(json_field "$LAST_BODY_FILE" "id")"

# ENDERECO
assert_http "endereco_create_success" "POST" "/usuarios/$usuario2_id/endereco" "201" '{"rua":"Rua A","numero":"10","bairro":"Centro","cidade":"Joao Pessoa","estado":"PB","cep":"58000-000"}'
assert_http "endereco_create_fail_400_duplicado" "POST" "/usuarios/$usuario2_id/endereco" "400" '{"rua":"Rua D","numero":"13","bairro":"Centro","cidade":"JP","estado":"PB","cep":"58003-000"}'
assert_http "endereco_get_success" "GET" "/usuarios/$usuario2_id/endereco" "200"
assert_http "endereco_put_success" "PUT" "/usuarios/$usuario2_id/endereco" "200" '{"rua":"Rua B","numero":"11","bairro":"Bairro B","cidade":"Joao Pessoa","estado":"PB","cep":"58001-000"}'
assert_http "endereco_delete_success" "DELETE" "/usuarios/$usuario2_id/endereco" "204"

assert_http "endereco_get_fail_404" "GET" "/usuarios/999999/endereco" "404"
assert_http "endereco_create_fail_400_usuario_invalido" "POST" "/usuarios/999999/endereco" "400" '{"rua":"Rua X","numero":"1","bairro":"B","cidade":"C","estado":"PB","cep":"58000-000"}'

# PET
assert_http "pet_create_success" "POST" "/pets" "201" "{\"nome\":\"Rex\",\"tipo\":\"Cachorro\",\"idade\":3,\"donoId\":$usuario2_id}"
pet_id="$(json_field "$LAST_BODY_FILE" "id")"

assert_http "pet_list_success" "GET" "/pets" "200"
assert_http "pet_list_by_dono_success" "GET" "/pets?donoId=$usuario2_id" "200"
assert_http "pet_get_success" "GET" "/pets/$pet_id" "200"
assert_http "pet_put_success" "PUT" "/pets/$pet_id" "200" "{\"nome\":\"Rex Atualizado\",\"tipo\":\"Cachorro\",\"idade\":4,\"donoId\":$usuario2_id}"
assert_http "pet_delete_success" "DELETE" "/pets/$pet_id" "204"

assert_http "pet_create_fail_400_dono_invalido" "POST" "/pets" "400" '{"nome":"Falha","tipo":"Gato","idade":2,"donoId":999999}'
assert_http "pet_get_fail_404" "GET" "/pets/999999" "404"
assert_http "pet_put_fail_404" "PUT" "/pets/999999" "404" "{\"nome\":\"X\",\"tipo\":\"Y\",\"idade\":1,\"donoId\":$usuario2_id}"
assert_http "pet_delete_fail_404" "DELETE" "/pets/999999" "404"

# VETERINARIO
assert_http "vet_create_success" "POST" "/veterinarios" "201" '{"nome":"Dr. Silva","crmv":"PB-1234"}'
veterinario_id="$(json_field "$LAST_BODY_FILE" "id")"

assert_http "vet_list_success" "GET" "/veterinarios" "200"
assert_http "vet_get_success" "GET" "/veterinarios/$veterinario_id" "200"
assert_http "vet_put_success" "PUT" "/veterinarios/$veterinario_id" "200" '{"nome":"Dr. Silva Atualizado","crmv":"PB-5678"}'

assert_http "vet_get_fail_404" "GET" "/veterinarios/999999" "404"
assert_http "vet_put_fail_404" "PUT" "/veterinarios/999999" "404" '{"nome":"N/A","crmv":"N/A"}'
assert_http "vet_delete_fail_404" "DELETE" "/veterinarios/999999" "404"

# Pet para CONSULTA
assert_http "pet2_create_success" "POST" "/pets" "201" "{\"nome\":\"Mimi\",\"tipo\":\"Gato\",\"idade\":2,\"donoId\":$usuario2_id}"
pet2_id="$(json_field "$LAST_BODY_FILE" "id")"

# CONSULTA
consulta_data_ok="2026-06-10T10:00:00"
consulta_data_new="2026-06-11T11:30:00"

assert_http "consulta_create_success" "POST" "/consultas" "201" "{\"veterinarioId\":$veterinario_id,\"petId\":$pet2_id,\"data\":\"$consulta_data_ok\"}"
consulta_id="$(python3 -c "import json; data=json.load(open('$LAST_BODY_FILE')); print(data.get('id', ''))" 2>/dev/null || echo '1')"

# Se não conseguir extrair ID da resposta de criação, buscar pela lista
if [[ -z "$consulta_id" || "$consulta_id" == "None" ]]; then
  assert_http "consulta_list_temp" "GET" "/consultas" "200"
  consulta_id="$(python3 -c "import json; data=json.load(open('$LAST_BODY_FILE')); print(data[0].get('id', '') if data else '')" 2>/dev/null || echo '1')"
fi

assert_http "consulta_list_success" "GET" "/consultas" "200"
assert_http "consulta_get_success" "GET" "/consultas/$consulta_id" "200"
assert_http "consulta_create_fail_409_duplicada" "POST" "/consultas" "409" "{\"veterinarioId\":$veterinario_id,\"petId\":$pet2_id,\"data\":\"$consulta_data_ok\"}"
assert_http "consulta_put_success" "PUT" "/consultas/$consulta_id" "200" "{\"veterinarioId\":$veterinario_id,\"petId\":$pet2_id,\"data\":\"$consulta_data_new\"}"
assert_http "consulta_delete_success" "DELETE" "/consultas/$consulta_id" "204"

assert_http "consulta_get_fail_404" "GET" "/consultas/999999" "404"
assert_http "consulta_put_fail_404" "PUT" "/consultas/999999" "404" "{\"veterinarioId\":$veterinario_id,\"petId\":$pet2_id,\"data\":\"$consulta_data_new\"}"
assert_http "consulta_delete_fail_404" "DELETE" "/consultas/999999" "404"
assert_http "consulta_create_fail_404_vet_invalido" "POST" "/consultas" "404" "{\"veterinarioId\":999999,\"petId\":$pet2_id,\"data\":\"$consulta_data_ok\"}"

# veterinario sem vinculo para validar delete com sucesso
assert_http "vet2_create_success" "POST" "/veterinarios" "201" '{"nome":"Dra Livre","crmv":"PB-9999"}'
veterinario2_id="$(json_field "$LAST_BODY_FILE" "id")"
assert_http "vet_delete_success" "DELETE" "/veterinarios/$veterinario2_id" "204"


echo "--- Resultado final ---"
echo "Passou: $pass_count"
echo "Falhou: $fail_count"

if [[ "$fail_count" -gt 0 ]]; then
  exit 1
fi


