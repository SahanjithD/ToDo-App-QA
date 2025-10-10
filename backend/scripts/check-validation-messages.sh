#!/usr/bin/env bash
# Fail if @NotBlank is used without a message or @Size used without message (simple heuristic)
set -euo pipefail

MISSING=0

# check @NotBlank occurrences
while IFS= read -r line; do
  file=$(echo "$line" | cut -d: -f1)
  code=$(echo "$line" | cut -d: -f2-)
  if [[ "$code" =~ "@NotBlank(" ]] && [[ ! "$code" =~ "message\s*=\s*\"" ]]; then
    echo "Missing message in @NotBlank in $file: $code"
    MISSING=1
  fi
done < <(grep -Rn "@NotBlank" backend/src || true)

# check @Size occurrences
while IFS= read -r line; do
  file=$(echo "$line" | cut -d: -f1)
  code=$(echo "$line" | cut -d: -f2-)
  if [[ "$code" =~ "@Size(" ]] && [[ ! "$code" =~ "message\s*=\s*\"" ]]; then
    echo "Missing message in @Size in $file: $code"
    MISSING=1
  fi
done < <(grep -Rn "@Size" backend/src || true)

if [ "$MISSING" -eq 1 ]; then
  echo "Validation message checks failed"
  exit 1
fi

echo "Validation message checks passed"
