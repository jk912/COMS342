#lang racket
(provide (all-defined-out))
(require racket/trace)

(define X 88)
(define Y 89)
(define Z 90)
(define adddef (lambda (n) (+ 32 n)))
(define DefineLang (lambda (letter)
                     (adddef letter)
                         ))

  
(define f (lambda (n)
  (if (= n 0)
      0
      (if (= n 1)
          1
          (+ (f (- n 1)) (f (- n 2)))))))


(define count (lambda (n lst)
  (if (null? lst)
      0
      (if (= n (car lst))
          (+ 1 (count n (cdr lst)))
          (count n (cdr lst))))
))



(define max (lambda (lst)
              (if (null? lst)
                  0
                  (if (null? (cdr lst))
                      (car lst)
                      (if (< (car lst) (car (cdr lst)))
                          (max (cdr lst))
                          (max (cons (car lst) (cdr (cdr lst))))
                          )
                      )
                  )
              )
  )



(define repeat (lambda (lst)
                 (if (null? lst)
                     '()
                     (cons (count (car lst) (cdr lst)) (repeat (cdr lst)))
                     )
                 )
  )


(define maxrepeat(lambda (lst)
                   (if (null? lst)
                       0
                       (+ 1 (max (repeat lst)))
                       )))


;;;;;;;; Q4 ;;;;;;;;;;;;;;;;
(define pairs
  (cons (cons 1 (list 5)) (cons (cons 6 (list 4)) (cons (cons 7 (list 8)) (list (cons 15 (list 10))))))
  )

(define pairsp
  (cons (cons 1 (list 4)) (list (cons 1 (list 5))))
  )
;;(secondSum '((1 5)(6 4)(7 8)(15 10))) ;;27
;; (secondSum '((1 3)))

(define (secondSum lst)
  (if (null? (cdr lst)) (car (cdr (car lst)))
      (+ (car (cdr (car lst))) (secondSum (cdr lst)))
      )
  )

(secondSum pairs) ;;27



(define pair2 (lambda (fst snd) (lambda (op) (if op fst snd))))
(define apair2 (pair2 2 3))

(define add
  (lambda (x y)
    (+ x y)
    )
  )

(define test
  (lambda (fst snd)
    (lambda (op)
      (op fst snd)
      )
    )
  )

(define pair
  (lambda (fst snd)
    (lambda (op)
      (op fst snd))))

(define bpair(pair2 2 3))
(define apair(pair 2 3))
(define first(lambda(p)(p #t)))
(define second(lambda(p)(p #f)))
(define FuncLang(lambda(p) (p add)))
;;(FuncLang apair)

(define mylist
  (cons (cons 1 (list 3)) (cons (cons 4 (list 2)) (list (cons 5 (list 6)))))
  )
(define subtract
  (lambda (x y)
    (- x y)))
;; (applyonnth add mylist 1)
(define applyonnth
  (lambda (op lst n)
    (if (null? lst)
        -1
        (if (= n 1)
            (op (car (car lst)) (car (cdr (car lst))))
            (applyonnth op (cdr lst) (- n 1))
            ))))
(define applyonnthcur
  (lambda (op) 
(lambda (lst) 
(lambda (n)
    (if (null? lst)
        -1
        (if (= n 1)
            (op (car (car lst)) (car (cdr (car lst))))
            (applyonnth op (cdr lst) (- n 1))
            ))))))


(applyonnth add mylist 3)
(applyonnth subtract mylist 3)

