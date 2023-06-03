package core.basesyntax;

import core.basesyntax.model.FruitModel;
import core.basesyntax.service.FileReaderService;
import core.basesyntax.service.FileWriterService;
import core.basesyntax.service.FruitShopService;
import core.basesyntax.service.ParserService;
import core.basesyntax.service.ReportMakerService;
import core.basesyntax.service.impl.FileReaderServiceImpl;
import core.basesyntax.service.impl.FileWriterServiceImpl;
import core.basesyntax.service.impl.FruitShopServiceImpl;
import core.basesyntax.service.impl.ParserServiceImpl;
import core.basesyntax.service.impl.ReportMakerServiceImpl;
import core.basesyntax.strategy.OperationStrategy;
import core.basesyntax.strategy.handler.OperationHandler;
import core.basesyntax.strategy.handler.impl.BalanceOperationHandler;
import core.basesyntax.strategy.handler.impl.OperationStrategyImpl;
import core.basesyntax.strategy.handler.impl.PurchaseOperationHandler;
import core.basesyntax.strategy.handler.impl.ReturnOperationHandler;
import core.basesyntax.strategy.handler.impl.SupplyOperationHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    private static final String READ_FILE_PATH = "src\\main\\resources\\data.csv";
    private static final String WRITE_FILE_PATH = "src\\main\\resources\\result.csv";
    private static final String DELIMITER = ",";
    private static final String HEADER = "fruit,quantity";

    public static void main(String[] args) {
        Map<FruitModel.Operation, OperationHandler> operationsHandlerMap = new HashMap<>();
        operationsHandlerMap.put(FruitModel.Operation.BALANCE, new BalanceOperationHandler());
        operationsHandlerMap.put(FruitModel.Operation.PURCHASE, new PurchaseOperationHandler());
        operationsHandlerMap.put(FruitModel.Operation.SUPPLY, new SupplyOperationHandler());
        operationsHandlerMap.put(FruitModel.Operation.RETURN, new ReturnOperationHandler());

        OperationStrategy strategy = new OperationStrategyImpl(operationsHandlerMap);
        FileReaderService fileReaderService = new FileReaderServiceImpl();
        ParserService parserService = new ParserServiceImpl();
        FruitShopService fruitShopService = new FruitShopServiceImpl(strategy);
        ReportMakerService reportMakerService = new ReportMakerServiceImpl(DELIMITER, HEADER);
        FileWriterService fileWriterService = new FileWriterServiceImpl();

        List<String> list = fileReaderService.readFromFile(READ_FILE_PATH);
        List<FruitModel> fruitModels = parserService.parseData(list);
        fruitShopService.processData(fruitModels);
        List<String> report = reportMakerService.makeReport();
        fileWriterService.writeToFile(WRITE_FILE_PATH, report);
    }
}