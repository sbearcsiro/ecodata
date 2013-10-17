package au.org.ala.ecodata.reporting

/**
 * An Aggregator is responsible for aggregating a list of scores.
 * It is capable of grouping the results by a single value.
 */
class Aggregator {

    static String DEFAULT_GROUP = ""

    Map<String, List<Aggregators.OutputAggregator>> aggregatorsByGroup
    List<Score> scores
    Closure groupingFunction
    String title

    public Aggregator(String title, groupingFunction, List<Score> scores, AggregatorBuilder builder) {

        this.groupingFunction = groupingFunction
        this.scores = scores
        this.title = title

        aggregatorsByGroup = [:].withDefault { key ->
            def aggregators = []
            scores.each { score ->
                aggregators << builder.createAggregator(score, key)
            }
            aggregators
        }
    }

    def aggregate(activity) {

        activity.outputs?.each { output ->
            List<Aggregators.OutputAggregator> aggregators = aggregatorFor(activity, output)
            aggregators.each{it.aggregate(output)}

        }

    }

    /**
     *  Returns the results of the aggregation.
     *  The results will be formatted like:
     *     {
     *         groupName: <title of grouping category>
     *         scores: [
     *             // For each score to by grouped by the criteria
     *             {
     *                 scoreLabel: <label of score>
     *                 outputLabel: <name of the output the score is collected under>
     *             },
     *             values: [
     *                 // For each unique value returned by the grouping criteria
     *                 {
     *                     aggregatedResult: <the result of aggregrating each value of the score for each group value>
     *                     groupValue: <the value of the grouping function>
     *                 }
     *             ]
     *         ]
     *     }
     */
    def results() {
        def results = []
        aggregatorsByGroup.values().each { aggregatorList ->
            results.addAll(aggregatorList.collect {
                aggregator -> aggregator.result()
            })
        }

        // Combine histogram based scores into a single result.
        def combinedResults = []
        def aggregatorsByScore = results.groupBy({it.outputName+':'+it.score})
        aggregatorsByScore.keySet().each{
            def value = aggregatorsByScore[it]
            if (value.size() == 1) {
                combinedResults << [outputLabel: value[0].outputName, scoreLabel:value[0].score, count:value[0].count,  aggregatedResult:value[0].result]
            } else {
                combinedResults << [outputLabel: value[0].outputName, scoreLabel:value[0].score, values:value.collect{ groupValue-> [aggregatedResult:groupValue.result, count:groupValue.count, groupValue:groupValue.group]} ]
            }
        }

        return [groupName:title, scores:combinedResults]
    }

    /**
     * Classifies the supplied output according to the groupingFunction and returns the
     * Aggregator(s) that are aggregrating results for that group.
     * @param activity the activity to be aggregated - this is optionally used to perform the grouping.
     * @param output the output containing the scores to be aggregated. The output itself may also be used by the
     * grouping function.
     */
    List<Aggregators.OutputAggregator> aggregatorFor(activity, output) {

        // TODO the grouping function should probably specify the default group.
        String group = groupingFunction(activity, output)
        if (!group) {
            group = DEFAULT_GROUP
        }

        return aggregatorsByGroup[group]
    }


}