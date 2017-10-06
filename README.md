# gossip - yet another lisp interpreter
> gossip是一个lisp解释器, 语法借鉴了scheme以及common lisp, 此项目的主要目的是学习。

## 安装
- 下载源码
- 打包: mvn package
- 运行方式: java -jar your_gossip_home/gossip-1.0-SNAPSHOT.jar (推荐将此命令alias为gossip)

## 运行模式
- REPL: gossip
- 解释器模式: gossip file_name.gossip

## 简明教程

#### gossip的bnf文法
文法主要参考自http://cui.unige.ch/isi/bnf/LISP/BNFlisp.html
```
 s_expr : list | atomic
 list: '(' s_expr < s_expr > ')'
 atomic: INT | FLOAT | STRING | NAME
```

#### 注释
> 使用"--"，进行行注释。

#### 标准输出
> 使用**print**命令，进行输出。

#### 全局变量
> 使用**setq**命令，定义全局变量。

语法如下：
```
(setq name value)
```

示例
```
gossip> (setq hello "hello gossip")
void
gossip> (print hello)
"hello gossip"
void
```

#### 局部变量
> 使用**let**命令，定义局部变量。

语法如下：
```
(let ((var1  val1) (var2  val2).. (varn  valn)) body)
```
示例
```
-- output: 3
(let ((p 1)(a 2)) (print (+ p a)))
```

#### 数据类型

- 整数(十进制)
- 浮点数
- 字符串
- Cons单元

```
gossip> (print 1)
1
void
gossip> (print 1.1)
1.1
void
gossip> (print "hello world")
"hello world"
void
gossip> (print (cons 1 (cons 2 3)))
(1 . (2 . 3))
void
gossip>
```

#### 生成表
> 函数cons给两个地址分配了内存空间, 第一个地址的内存空间被称作car部分，另一个地址的内存空间被称作cdr部分。

```
-- 使用cons进行构造, 并且可以嵌套构造。
(setq a (cons 1
    (cons 2 3)))
 
-- 使用car命令，获取第1个地址空间, output: 1
(print (car a))
 
-- 使用cdr命令，获取第2个地址空间, output: (2 . 3)
(print (cdr a))
```

#### 运算符
> 运算符包括
+ 算术运算符
    + 加法 +
    + 减法 -
    + 乘法 *
    + 除法 /
+ 比较运算符
    + 大于 >
    + 小于 <
    + 等于 =
    + 大于等于 >=
    + 小于等于 <=
+ 逻辑运算符
    + 与 and
    + 或 or
    + 非 not

#### 决策
> 使用**cond**进行分支策略

cond语法如下：
```
(cond (test1 action1)
      (test2 action2)
	  ...
	  (testn actionn))
```

示例

```
(setq step 100)
 
-- cond, output: "greater than 99"
(cond ((> step 99) (print "greater than 99"))
      ((> step 101) (print "greater than 101")))
```

#### 循环
> 递归就好了:P

#### 函数
> 使用**define**进行函数定义

语法如下:
```
(define (name arg1 arg2... argn) body)
```

示例

```
(define (addOne x)
    (+ x 1))
    
-- output: 4
(print (addOne 3))
```

当然，函数是支持**递归**的，比如我们可以实现一个菲波那切函数。
```
(define (fibonacci n)
    (cond ((= n 1) 1)
          ((= n 2) 1)
          ((> n 2) (+ (fibonacci (- n 2)) (fibonacci (- n 1))))))
          
-- output: 8
(print (fibonacci 6))
```

> 使用**lambda**进行定义

lambda语法如下：
```
(lambda (param) body)
```
我们也可以直接使用lambda进行函数定义。
```
(define addOne
    (lambda (x) (+ x 1)))
 
-- output: 2
(print (addOne 1))
```

#### 高阶函数
> 用作过程抽象，一般有两个含义：1）函数作为入参 2）函数作为返回值

比如，我们可以写一个大名鼎鼎的compose函数。
```
(define (compose2 f g)
    (lambda (x) (f (g x))))
    
(define (add x) (+ x 1))
 
 -- 更优雅的方式，应该是pointfree风格：(define addTwice (compose2 add add))。然而，现在还不支持:(
(define (addTwice x)
    ((compose2 add add) x))
 
-- output: 3
(print (addTwice 1))

```
