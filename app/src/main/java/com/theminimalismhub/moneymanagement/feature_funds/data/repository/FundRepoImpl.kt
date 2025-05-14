package com.theminimalismhub.moneymanagement.feature_funds.data.repository

import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance
import com.theminimalismhub.moneymanagement.feature_funds.data.data_source.FundDao
import com.theminimalismhub.moneymanagement.feature_funds.data.model.FundAccountCrossRef
import com.theminimalismhub.moneymanagement.feature_funds.data.model.FundCategoryCrossRef
import com.theminimalismhub.moneymanagement.feature_funds.data.model.FundFinanceCrossRef
import com.theminimalismhub.moneymanagement.feature_funds.data.model.FundItem
import com.theminimalismhub.moneymanagement.feature_funds.domain.model.Fund
import com.theminimalismhub.moneymanagement.feature_funds.domain.repository.FundRepo
import kotlinx.coroutines.flow.Flow

class FundRepoImpl (
    private val dao: FundDao
) : FundRepo {

    override fun getAll(): Flow<List<Fund>> {
        return dao.getFunds()
    }

    override suspend fun insert(item: FundItem, categories: List<Category>, accounts: List<Account>, finances: List<Finance>) {
        val fundId: Int = if(item.fundId == null) {
            dao.insertFund(item).toInt()
        } else {
            dao.updateFund(item)
            dao.deleteCategoryRefs(item.fundId)
            dao.deleteAccountRefs(item.fundId)
            dao.deleteFinanceRefs(item.fundId)
            item.fundId
        }
        categories.forEach { c ->
            dao.insertCategoryRef(FundCategoryCrossRef(
                fundId = fundId,
                categoryId = c.categoryId!!
            ))
        }
        accounts.forEach { a ->
            dao.insertAccountRef(FundAccountCrossRef(
                fundId = fundId,
                accountId = a.accountId!!
            ))
        }
        finances.forEach { f ->
            dao.insertFinanceRef(FundFinanceCrossRef(
                fundId = fundId,
                financeId = f.finance.financeId!!
            ))
        }
    }
}