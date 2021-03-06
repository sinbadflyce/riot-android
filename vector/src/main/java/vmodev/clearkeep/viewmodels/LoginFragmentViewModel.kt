package vmodev.clearkeep.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import vmodev.clearkeep.repositories.LoginRepository
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodels.interfaces.AbstractLoginFragmentViewModel
import javax.inject.Inject

class LoginFragmentViewModel @Inject constructor(loginRepository: LoginRepository) : AbstractLoginFragmentViewModel() {
    private val _setUserNamePasswordForLogin = MutableLiveData<LoginObject>();
    private val _getLoginResult = Transformations.switchMap(_setUserNamePasswordForLogin) { input -> loginRepository.login(input.username, input.password) }
    override fun setUserNamePasswordForLogin(username: String, password: String) {
        _setUserNamePasswordForLogin.value = LoginObject(username, password);
    }

    override fun getLoginResult(): LiveData<Resource<String>> {
        return _getLoginResult;
    }
}