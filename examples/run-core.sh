#!/usr/bin/env bash
set -euo pipefail

tutorial_root="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
selection="${1:-help}"
if [[ "$selection" == "all" ]]; then
    for chapter in 10 16 cancellation 24 32 33 34 36; do
        printf '\n예제 %s\n' "$chapter"
        bash "$tutorial_root/run-core.sh" "$chapter"
    done
    exit 0
fi
case "$selection" in
    10) chapter=10; class_name=CollectionsExample ;;
    16) chapter=16; class_name=ConcurrencyExample ;;
    cancellation) chapter=16; class_name=CancellationExample ;;
    24) chapter=24; class_name=StockExample ;;
    32) chapter=32; class_name=EqualityExample ;;
    33) chapter=33; class_name=ImmutabilityExample ;;
    34) chapter=34; class_name=DateMoneyExample ;;
    36) chapter=36; class_name=RefactoringExample ;;
    *)
        printf '사용법: bash examples/run-core.sh {10|16|cancellation|24|32|33|34|36|all}\n'
        printf 'Part 35와 JUnit 테스트는 examples/README.md의 Gradle 안내를 참고하세요.\n'
        [[ "$selection" == "help" ]] && exit 0
        exit 2
        ;;
esac
command -v javac >/dev/null || { printf 'JDK 17 이상의 javac가 필요합니다.\n' >&2; exit 1; }
command -v java >/dev/null || { printf 'java 실행 명령을 찾을 수 없습니다.\n' >&2; exit 1; }
class_dir="$tutorial_root/build/core/classes"
mkdir -p "$class_dir"
javac -encoding UTF-8 --release 17 -d "$class_dir" \
    "$tutorial_root/src/main/java/tutorial/part$chapter/"*.java
java -Dfile.encoding=UTF-8 -cp "$class_dir" "tutorial.part$chapter.$class_name"
