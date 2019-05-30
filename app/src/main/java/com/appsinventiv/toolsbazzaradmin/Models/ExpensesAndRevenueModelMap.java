package com.appsinventiv.toolsbazzaradmin.Models;

import com.appsinventiv.toolsbazzaradmin.Activities.Accounts.ExpensesAndRevenue.Miscellaneous;
import com.appsinventiv.toolsbazzaradmin.Activities.Accounts.ExpensesAndRevenue.Rent;

/**
 * Created by AliAh on 28/10/2018.
 */

public class ExpensesAndRevenueModelMap {
    private MiscellaneousModel Miscellaneous;
    private RentModel Rent;
    private SalaryModel Salaries;
    private StationariesModel Stationaries;
    private TransportationModel Transportation;
    private UtilityBillsModel UtilityBills;

    private float TotalExpense;

    public MiscellaneousModel getMiscellaneous() {
        return Miscellaneous;
    }

    public void setMiscellaneous(MiscellaneousModel miscellaneous) {
        Miscellaneous = miscellaneous;
    }

    public float getTotalExpense() {
        TotalExpense = 0;
        TotalExpense = Rent.getTotal() + UtilityBills.getTotal() + Stationaries.getTotal() + Transportation.getTotal()
                + Miscellaneous.getTotal();

        return TotalExpense;
    }

    public RentModel getRent() {
        return Rent;
    }

    public void setRent(RentModel rent) {
        Rent = rent;
    }

    public SalaryModel getSalaries() {
        return Salaries;
    }

    public void setSalaries(SalaryModel salaries) {
        Salaries = salaries;
    }

    public StationariesModel getStationaries() {
        return Stationaries;
    }

    public void setStationaries(StationariesModel stationaries) {
        Stationaries = stationaries;
    }

    public TransportationModel getTransportation() {
        return Transportation;
    }

    public void setTransportation(TransportationModel transportation) {
        Transportation = transportation;
    }

    public UtilityBillsModel getUtilityBills() {
        return UtilityBills;
    }

    public void setUtilityBills(UtilityBillsModel utilityBills) {
        UtilityBills = utilityBills;
    }
}
