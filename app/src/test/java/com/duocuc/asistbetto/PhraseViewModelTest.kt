package com.duocuc.asistbetto

import com.duocuc.asistbetto.data.model.Phrase
import com.duocuc.asistbetto.data.model.UserProfile
import com.duocuc.asistbetto.data.repository.AuthRepository
import com.duocuc.asistbetto.data.repository.PhraseRepository
import com.duocuc.asistbetto.ui.viewmodel.PhraseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PhraseViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private class FakeAuthRepo : AuthRepository {
        override suspend fun register(
            profile: UserProfile,
            password: String
        ): Result<Unit> {
            TODO("Not yet implemented")
        }

        override suspend fun login(
            email: String,
            password: String
        ): Result<Unit> {
            TODO("Not yet implemented")
        }

        override suspend fun recoverPassword(email: String): Result<Unit> {
            TODO("Not yet implemented")
        }

        override fun currentUid(): String? = "uid_test"
        override fun logout() {
            TODO("Not yet implemented")
        }
    }

    private class FakePhraseRepo : PhraseRepository {
        private val storage = mutableListOf<Phrase>()

        override suspend fun addPhrase(uid: String, phrase: Phrase): Result<Unit> {
            val id = if (phrase.id.isBlank()) "id_${storage.size + 1}" else phrase.id
            storage.add(phrase.copy(id = id))
            return Result.success(Unit)
        }

        override suspend fun listPhrases(uid: String, limit: Long): Result<List<Phrase>> {
            return Result.success(storage.toList())
        }

        override suspend fun deletePhrase(uid: String, phraseId: String): Result<Unit> {
            storage.removeAll { it.id == phraseId }
            return Result.success(Unit)
        }
    }

    @Test
    fun `load trae frases del repo`() = runTest {
        val repo = FakePhraseRepo()
        repo.addPhrase("uid_test", Phrase(text = "Hola", type = "WRITE"))
        repo.addPhrase("uid_test", Phrase(text = "Buenos dias", type = "SPEAK"))

        val vm = PhraseViewModel(FakeAuthRepo(), repo)

        vm.load()

        val phrases = vm.phrases.value
        assertEquals(2, phrases.size)
    }

    @Test
    fun `add NO permite duplicados por texto normalizado y type`() = runTest {
        val repo = FakePhraseRepo()
        val vm = PhraseViewModel(FakeAuthRepo(), repo)

        vm.load()

        vm.add("   Hola   mundo  ", "WRITE")
        vm.add("hola mundo", "WRITE") // duplicado
        vm.add("hola mundo", "SPEAK") // distinto type, permitido

        val phrases = vm.phrases.value
        assertEquals(2, phrases.size)
        assertTrue(phrases.any { it.type == "WRITE" })
        assertTrue(phrases.any { it.type == "SPEAK" })
    }

    @Test
    fun `delete elimina y refresca`() = runTest {
        val repo = FakePhraseRepo()
        val vm = PhraseViewModel(FakeAuthRepo(), repo)

        vm.add("Uno", "WRITE")
        vm.add("Dos", "WRITE")

        val before = vm.phrases.value
        assertEquals(2, before.size)

        val idToDelete = before.first().id
        vm.delete(idToDelete)

        val after = vm.phrases.value
        assertEquals(1, after.size)
    }
}