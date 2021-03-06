package vmodev.clearkeep.repositories

import androidx.lifecycle.LiveData
import io.reactivex.Observable
import vmodev.clearkeep.databases.AbstractSignatureDao
import vmodev.clearkeep.matrixsdk.interfaces.MatrixService
import vmodev.clearkeep.repositories.wayloads.AbstractNetworkBoundSourceRx
import vmodev.clearkeep.viewmodelobjects.Resource
import vmodev.clearkeep.viewmodelobjects.Signature
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignatureRepository @Inject constructor(private val matrixService: MatrixService, private val signatureDao: AbstractSignatureDao) {
    fun getAllSignature(id: String): LiveData<Resource<List<Signature>>> {
        return object : AbstractNetworkBoundSourceRx<List<Signature>, List<Signature>>() {
            override fun saveCallResult(item: List<Signature>) {
                signatureDao.insertList(item);
            }

            override fun shouldFetch(data: List<Signature>?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<List<Signature>> {
                return signatureDao.getListSignatureFromKeyBackup(id);
            }

            override fun createCall(): Observable<List<Signature>> {
                return matrixService.getListSignature(id);
            }
        }.asLiveData();
    }

    fun updateSignature(id: String): LiveData<Resource<List<Signature>>> {
        return object : AbstractNetworkBoundSourceRx<List<Signature>, List<Signature>>() {
            override fun saveCallResult(item: List<Signature>) {
                signatureDao.updateList(item);
            }

            override fun shouldFetch(data: List<Signature>?): Boolean {
                return true;
            }

            override fun loadFromDb(): LiveData<List<Signature>> {
                return signatureDao.getListSignatureFromKeyBackup(id);
            }

            override fun createCall(): Observable<List<Signature>> {
                return matrixService.getListSignature(id);
            }
        }.asLiveData();
    }
}