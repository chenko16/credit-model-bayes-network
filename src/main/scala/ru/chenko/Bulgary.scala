package ru.chenko;

import com.cra.figaro.algorithm.factored._
import com.cra.figaro.language._
import com.cra.figaro.library.compound._

/**
 * A Bayesian network example
 */
object CreditModel {
  Universe.createNew()

  //root nodes
  var employedSpouseNode = Select(0.6 -> true, 0.4 -> false)
  var incomeNode = Select(0.3 -> 'low, 0.5 -> 'medium, 0.2 -> 'high)
  var reliabilityOfEmployerNode = Select(0.6 -> 'high, 0.4 -> 'low)

  val deadlyDiseaseNode = Select(0.85 -> false, 0.15 -> true)
  val ratioOfDebtToIncomeNode = Select(0.7 -> 'high, 0.3 -> 'low)
  val ageNode = Select(0.2 -> 'between18and22, 0.6 -> 'between23and59, 0.2 -> 'over60)


  //internal nodes
  val assetsNode = RichCPD(incomeNode, employedSpouseNode,
    (OneOf('high), OneOf(true)) -> Select(0.6 -> 'high, 0.2 -> 'medium, 0.2 -> 'low),
    (OneOf('high), OneOf(false)) -> Select(0.8 -> 'high, 0.15 -> 'medium, 0.05 -> 'low),
    (OneOf('medium), OneOf(true)) -> Select(0.6 -> 'high, 0.3 -> 'medium, 0.1 -> 'low),
    (OneOf('medium), OneOf(false)) -> Select(0.4 -> 'high, 0.3 -> 'medium, 0.3 -> 'low),
    (OneOf('low), OneOf(true)) -> Select(0.3 -> 'high, 0.3 -> 'medium, 0.4 -> 'low),
    (OneOf('low), OneOf(false)) -> Select(0.1 -> 'high, 0.3 -> 'medium, 0.6 -> 'low)
  )


  val futureIncomeNode = RichCPD(assetsNode, incomeNode, reliabilityOfEmployerNode,
    (OneOf('high), OneOf('high), OneOf('high)) -> Select(0.9 -> 'promising, 0.1 -> 'not_promising),
    (OneOf('high), OneOf('high), OneOf('low)) -> Select(0.8 -> 'promising, 0.2 -> 'not_promising),
    (OneOf('high), OneOf('medium), OneOf('high)) -> Select(0.8 -> 'promising, 0.2 -> 'not_promising),
    (OneOf('high), OneOf('medium), OneOf('low)) -> Select(0.7 -> 'promising, 0.3 -> 'not_promising),
    (OneOf('high), OneOf('low), OneOf('high)) -> Select(0.7 -> 'promising, 0.3 -> 'not_promising),
    (OneOf('high), OneOf('low), OneOf('low)) -> Select(0.6 -> 'promising, 0.4 -> 'not_promising),

    (OneOf('medium), OneOf('high), OneOf('high)) -> Select(0.6 -> 'promising, 0.4 -> 'not_promising),
    (OneOf('medium), OneOf('high), OneOf('low)) -> Select(0.5 -> 'promising, 0.5 -> 'not_promising),
    (OneOf('medium), OneOf('medium), OneOf('high)) -> Select(0.5 -> 'promising, 0.5 -> 'not_promising),
    (OneOf('medium), OneOf('medium), OneOf('low)) -> Select(0.4 -> 'promising, 0.6 -> 'not_promising),
    (OneOf('medium), OneOf('low), OneOf('high)) -> Select(0.4 -> 'promising, 0.6 -> 'not_promising),
    (OneOf('medium), OneOf('low), OneOf('low)) -> Select(0.3 -> 'promising, 0.7 -> 'not_promising),

    (OneOf('low), OneOf('high), OneOf('high)) -> Select(0.3 -> 'promising, 0.7 -> 'not_promising),
    (OneOf('low), OneOf('high), OneOf('low)) -> Select(0.2 -> 'promising, 0.8 -> 'not_promising),
    (OneOf('low), OneOf('medium), OneOf('high)) -> Select(0.2 -> 'promising, 0.8 -> 'not_promising),
    (OneOf('low), OneOf('medium), OneOf('low)) -> Select(0.1 -> 'promising, 0.9 -> 'not_promising),
    (OneOf('low), OneOf('low), OneOf('high)) -> Select(0.1 -> 'promising, 0.9 -> 'not_promising),
    (OneOf('low), OneOf('low), OneOf('low)) -> Select(0.01 -> 'promising, 0.99 -> 'not_promising)
  )


  val paymentHistoryNode = RichCPD(ageNode, ratioOfDebtToIncomeNode,
    (OneOf('between18and22), OneOf('low)) -> Select(0.15 -> 'excellent, 0.25 -> 'acceptable, 0.6 -> 'unacceptable),
    (OneOf('between18and22), OneOf('high)) -> Select(0.1 -> 'excellent, 0.2 -> 'acceptable, 0.7 -> 'unacceptable),
    (OneOf('between23and59), OneOf('low)) -> Select(0.3 -> 'excellent, 0.4 -> 'acceptable, 0.3 -> 'unacceptable),
    (OneOf('between23and59), OneOf('high)) -> Select(0.2 -> 'excellent, 0.3 -> 'acceptable, 0.5 -> 'unacceptable),
    (OneOf('over60), OneOf('low)) -> Select(0.4 -> 'excellent, 0.5 -> 'acceptable, 0.1 -> 'unacceptable),
    (OneOf('over60), OneOf('high)) -> Select(0.35 -> 'excellent, 0.45 -> 'acceptable, 0.2 -> 'unacceptable)
  )


  val reliabilityNode = RichCPD(paymentHistoryNode, ageNode, deadlyDiseaseNode,
    (OneOf('excellent), OneOf('between18and22), OneOf(false)) -> Select(0.7 -> true, 0.3 -> false),
    (OneOf('excellent), OneOf('between18and22), OneOf(true)) -> Select(0.65 -> true, 0.35 -> false),
    (OneOf('excellent), OneOf('between23and59), OneOf(false)) -> Select(0.8 -> true, 0.2 -> false),
    (OneOf('excellent), OneOf('between23and59), OneOf(true)) -> Select(0.75 -> true, 0.25 -> false),
    (OneOf('excellent), OneOf('over60), OneOf(false)) -> Select(0.9 -> true, 0.1 -> false),
    (OneOf('excellent), OneOf('over60), OneOf(true)) -> Select(0.85 -> true, 0.15 -> false),

    (OneOf('acceptable), OneOf('between18and22), OneOf(false)) -> Select(0.4 -> true, 0.6 -> false),
    (OneOf('acceptable), OneOf('between18and22), OneOf(true)) -> Select(0.35 -> true, 0.65 -> false),
    (OneOf('acceptable), OneOf('between23and59), OneOf(false)) -> Select(0.5 -> true, 0.5 -> false),
    (OneOf('acceptable), OneOf('between23and59), OneOf(true)) -> Select(0.45 -> true, 0.55 -> false),
    (OneOf('acceptable), OneOf('over60), OneOf(false)) -> Select(0.6 -> true, 0.4 -> false),
    (OneOf('acceptable), OneOf('over60), OneOf(true)) -> Select(0.55 -> true, 0.45 -> false),

    (OneOf('unacceptable), OneOf('between18and22), OneOf(false)) -> Select(0.1 -> true, 0.9 -> false),
    (OneOf('unacceptable), OneOf('between18and22), OneOf(true)) -> Select(0.05 -> true, 0.95 -> false),
    (OneOf('unacceptable), OneOf('between23and59), OneOf(false)) -> Select(0.2 -> true, 0.8 -> false),
    (OneOf('unacceptable), OneOf('between23and59), OneOf(true)) -> Select(0.15 -> true, 0.85 -> false),
    (OneOf('unacceptable), OneOf('over60), OneOf(false)) -> Select(0.3 -> true, 0.7 -> false),
    (OneOf('unacceptable), OneOf('over60), OneOf(true)) -> Select(0.25 -> true, 0.75 -> false),
  )


  val creditWorthinessNode = RichCPD(reliabilityNode, futureIncomeNode, ratioOfDebtToIncomeNode,
    (OneOf(true), OneOf('promising), OneOf('low)) -> Select(0.9 -> true, 0.1 -> false),
    (OneOf(true), OneOf('promising), OneOf('high)) -> Select(0.8 -> true, 0.2 -> false),
    (OneOf(true), OneOf('not_promising), OneOf('low)) -> Select(0.7 -> true, 0.3 -> false),
    (OneOf(true), OneOf('not_promising), OneOf('high)) -> Select(0.6 -> true, 0.4 -> false),

    (OneOf(false), OneOf('promising), OneOf('low)) -> Select(0.5 -> true, 0.5 -> false),
    (OneOf(false), OneOf('promising), OneOf('high)) -> Select(0.4 -> true, 0.6 -> false),
    (OneOf(false), OneOf('not_promising), OneOf('low)) -> Select(0.3 -> true, 0.7 -> false),
    (OneOf(false), OneOf('not_promising), OneOf('high)) -> Select(0.2 -> true, 0.8 -> false)
  )



  def main(args: Array[String]) {
    deadlyDiseaseNode.observe(true)
    reliabilityOfEmployerNode.observe('high)
    futureIncomeNode.observe('not_promising)
    val alg = VariableElimination(creditWorthinessNode)
    alg.start()
    println("Probability of burglary: " + alg.probability(creditWorthinessNode, true))
    alg.kill
  }
}
