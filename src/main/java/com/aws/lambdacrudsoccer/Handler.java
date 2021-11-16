package com.aws.lambdacrudsoccer;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.ArrayList;
import java.util.List;

public class Handler implements RequestHandler<Request, Object> {
    @Override
    public Object handleRequest(Request request, Context context) {

        AmazonDynamoDB db = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper dbMapper = new DynamoDBMapper(db);
        Team team = null;

        switch (request.getHttpMethod()) {
            case "GET":
                if (request.getId() == 0) {
                    List<Team> teams = new ArrayList<>();
                    teams = dbMapper.scan(Team.class, new DynamoDBScanExpression());
                    return teams;
                } else {
                    team = dbMapper.load(Team.class, request.getId());
                    return team;
                }

            case "POST":
                team = request.getTeam();
                dbMapper.save(team);
                return team;
            case "DELETE":
                team = dbMapper.load(Team.class, request.getId());
                dbMapper.delete(team);
                return team;
        }

        return null;
    }


}
