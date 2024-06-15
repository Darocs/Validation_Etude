This is an etude about our validation approach in android projects
It consists of best practices used by our team including
* value objects validation with type-safe errors
* only one check at a time: fail-first [Secure by Design](https://www.manning.com/books/secure-by-design)
* errors in check result are not throwable but typed errors of sealed classes
* [parse, don't validate](https://lexi-lambda.github.io/blog/2019/11/05/parse-don-t-validate/) and

About Etudes series. Etudes were established to reflect the best practices developed by our team in order to use in our projects.
We try to keep Etudes as a reference both with docs and code as well with sufficient tests.
We understand Etude with both musical and painting essentials - to prepare a part of masterpiece and to practice for perfecting a skill.


### Sources
* *lib:* [Konform](https://github.com/konform-kt/konform) - Portable validations for Kotlin
* *article:* [Parse, don’t validate](https://lexi-lambda.github.io/blog/2019/11/05/parse-don-t-validate/)
* *lib:* [Tribune](https://github.com/sksamuel/tribune/tree/master) - parse don't validate for Kotlin.
* *lib:* [Arrow](https://arrow-kt.io/learn/typed-errors/validation/) Validation
* *article:* [Domain Model Validation In Kotlin: Part 1](https://tibtof.medium.com/domain-model-validation-in-kotlin-part-1-21fa44c60ef3)
* *article:* [Object validation in Ktor/Kotlin](https://medium.com/nerd-for-tech/object-validation-in-kotlin-c7e02b5dabc)