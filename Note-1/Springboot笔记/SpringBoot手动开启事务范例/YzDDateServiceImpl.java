
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Service
public class YzDDateServiceImpl implements YzDDateService {

    @Resource
    private DataSourceTransactionManager transactionManager;
    @Override
    public MobileBaseResponse settings(MobileBaseRequest<YzDDateRequest> mobileRequest) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try{
            if (xxx == null){
				//rollback
                transactionManager.rollback(status);
				return ...;
            }

			if(xxx != null){
				//commit
				transactionManager.commit(status);
			}


            transactionManager.rollback(status);
        } catch (Exception e) {
            e.printStackTrace();
            transactionManager.rollback(status);
            return mobileBaseResponse;
        }
    }

