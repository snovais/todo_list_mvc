# 📋 Todo App

Aplicativo de gerenciamento de tarefas desenvolvido em Java com interface gráfica Swing e persistência em SQLite.

## ✨ Funcionalidades

- ✅ Adicionar tarefas com título, matéria e prazo
- 🚦 Semáforo de urgência (manual ou automático por prazo)
- ✔️ Marcar tarefas como concluídas
- 🔍 Busca em tempo real por título ou matéria
- 🔃 Ordenação automática por prazo
- 🗑 Remover tarefas individualmente ou limpar concluídas
- 💾 Persistência automática em banco SQLite

## 🏗 Arquitetura

O projeto segue o padrão **MVC (Model-View-Controller)**:
```
br.com.todo/
├── model/          → Task, Urgencia
├── repository/     → TaskRepository (interface), TaskRepositorySQL
├── controller/     → TaskController
├── database/       → DatabaseManager
├── view/           → AppFrame, HeaderPanel, FormPanel, ListPanel, FooterPanel
└── TodoApp.java    → ponto de entrada
```

## 🛠 Tecnologias

- Java 21
- Swing (interface gráfica)
- SQLite (banco de dados)
- Maven (gerenciamento de dependências)
- JUnit 5 (testes)

## 🚀 Como rodar

### Pré-requisitos
- JDK 21
- Maven 3.x

### Clonar e rodar
```bash
git clone https://github.com/seu-usuario/todo-app.git
cd todo-app
mvn compile && mvn exec:java
```

### Rodar os testes
```bash
mvn test
```

### Gerar instalador (Linux)
```bash
mvn package -DskipTests
mvn dependency:copy-dependencies -DoutputDirectory=target/dependency-jars
jpackage \
  --input target \
  --dest instalador \
  --name "TodoApp" \
  --main-jar todo-app-mvc-1.0-SNAPSHOT.jar \
  --main-class br.com.todo.TodoApp \
  --type deb \
  --icon src/main/resources/icon.png \
  --app-version 1.0 \
  --vendor "Sérgio Lima" \
  --linux-shortcut \
  --java-options "-Dsun.java2d.uiScale=2.0"
```

## 📦 Instaladores

Os instaladores são gerados automaticamente pelo GitHub Actions a cada push na branch `main`:

- 🐧 **Linux** → `.deb`
- 🪟 **Windows** → `.exe`

Acesse a aba **Actions** no GitHub e baixe os artefatos após o build.

## 🧪 Testes

O projeto possui testes unitários para as camadas de modelo e controller:

- `TaskTest` — testa criação, urgência, prazo e matéria
- `TaskControllerTest` — testa adicionar, remover, ordenar e limpar

## 👨‍💻 Autor

**Sérgio Lima**  
Desenvolvedor Java — 2026