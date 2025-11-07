# Free

```text
源码 (.free)
   ↓
词法分析（Lexer） → Tokens
   ↓
语法分析（Parser） → AST（抽象语法树）
   ↓
语义分析（Semantic Analyzer）
   ↓
中间代码生成（IR / Bytecode / AST优化）
   ↓
代码生成（目标平台：.cfree、.wasm、.native 等）
   ↓
可执行 / 解释运行
```

```text
AstNode
├── Program
│    └── SourceFileNode
│         ├── PackageDeclaration
│         └── Declaration*
│              ├── FunctionDeclaration
│              │    └── List<Statement>
│              ├── ConstantDeclaration
│              ├── ConstantFunctionDeclaration
│              ├── ClassDeclaration
│              │    ├── ConstructorDeclaration
│              │    └── List<Declaration>
│              ├── StructDeclaration
│              └── PropertyDeclaration
│
├── Statement
│    ├── VariableDeclaration
│    ├── AssignmentStatement
│    ├── ExpressionStatement
│    └── ReturnStatement
│
└── Expression
     ├── LiteralExpression
     ├── BinaryExpression
     ├── UnaryExpression
     ├── FunctionCallExpression
     ├── LambdaExpression
     ├── FunctionReferenceExpression
     ├── MemberReferenceExpression
     ├── ArrayExpression
     ├── StructInitializationExpression
     ├── ObjectCreationExpression
     ├── PropertyAccessExpression
     └── NamedArgumentExpression
```